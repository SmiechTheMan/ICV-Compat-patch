package net.igneo.icv.shader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

public class BlinkShader extends PostProcessor {
    public static final BlinkShader INSTANCE = new BlinkShader();

    @Override
    public ResourceLocation getPostChainLocation() {
        return new ResourceLocation(ICV.MOD_ID, "tint_post");
    }

    @Override
    public void beforeProcess(PoseStack poseStack) {

    }

    @Override
    public void afterProcess() {

    }
}
