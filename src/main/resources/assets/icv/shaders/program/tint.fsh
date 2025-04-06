#version 150
// The game's render output
uniform sampler2D DiffuseSampler;

uniform float Time;
// The texture coordinate represented as a 2D vector (x,y)
in vec2 texCoord;
// The output color of each pixel represented as a 4D vector (r,g,b,a)
out vec4 fragColor;

void main() {
    // Extract the original color of the pixel from the DiffuseSampler
    vec4 original = texture(DiffuseSampler, texCoord);

    float t = sin(3.1415*Time)/2;

    float r = 1.0-(t*1.75);
    float g = 0.0+t;
    float b = 0.0+(t*2);

    // Pink! //nah, ourple
    vec3 tintColor = vec3(r,g,b);
    // Multiply each rgba value by the tint color.
    vec4 result = original * vec4(tintColor, 1.0);

    // Set the fragColor output as the result
    fragColor = result;
}