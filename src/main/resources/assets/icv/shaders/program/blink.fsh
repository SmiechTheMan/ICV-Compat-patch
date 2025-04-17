#version 150

#moj_import <lodestone:common_math.glsl>

// Samplers
uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;
// Multi-Instance uniforms
uniform samplerBuffer DataBuffer;
uniform int InstanceCount;
// Matrices needed for world position calculation
uniform mat4 invProjMat;
uniform mat4 invViewMat;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform vec3 cameraPos;

in vec2 texCoord;
out vec4 fragColor;

bool skip = false;

vec2 worldPosToTexCoord(vec3 worldPos, mat4 invViewMat, mat4 invProjMat, vec3 cameraPos) {
    mat4 viewMat = inverse(invViewMat);
    mat4 projMat = inverse(invProjMat);
    vec3 localPos = worldPos - cameraPos;

    vec4 clipSpace = projMat * viewMat * vec4(localPos, 1.0);
    vec4 ndc = clipSpace / clipSpace.w;

    if (clipSpace.w <= 0.0) {
        skip = true;
    }

    return ndc.xy * 0.5 + 0.5;
}

void main() {

    //setting up the screen to then distort it after
    vec4 diffuseColor = vec4(texture(DiffuseSampler, texCoord));
    fragColor = diffuseColor;

    for (int instance = 0; instance < InstanceCount; instance++) {

        //collecting instance, center, and color values
        int index = instance * 7;
        vec3 center = fetch3(DataBuffer, index);
        vec3 color = fetch3(DataBuffer, index + 3);

        //converting the worldpos of center into usable texcoord
        vec2 centerCoord = worldPosToTexCoord(center, invViewMat, invProjMat, cameraPos);

        //getting the aspect ratio to lie to the screen about where the center is so that a sphere is rendered instead of an oval
        float aspect = InSize.x / InSize.y;
        vec2 scaledTexCoord = vec2(texCoord.x, texCoord.y / aspect);
        vec2 scaledCenter = vec2(centerCoord.x, centerCoord.y / aspect);
        float distance = length(scaledTexCoord - scaledCenter);

        //collecting my per-instance time variable to scale the radius for the in-and-out motion of the effect
        float instTime = fetch(DataBuffer, index+6);
        float t = pow(abs(((sin(3.1415*instTime*2))) + 0.1), 2);
        float radius = (1 / length(center - cameraPos))*t;

        if (!skip && distance <= radius) {

            //calculating the distance of the pixel to the center to determine how far i should move which pixel it reads
            float falloff = pow(clamp((distance*2) / radius, 0.0, 1.0), 2);
            vec2 warpedTex = texCoord + (texCoord - 0.5) * (1.0 - falloff);
            fragColor.rgb = texture(DiffuseSampler, warpedTex).rgb;

            //calculating the distance of the pixel to the center to determine how colorful it should be
            float colorfalloff = pow(clamp(distance / radius, 0.0, 1.0), 3);
            fragColor.rgb *= (color*2 * colorfalloff + 1.0);
        }
    }
}