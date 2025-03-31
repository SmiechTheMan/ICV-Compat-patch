package net.igneo.icv.entity.surfWave;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.soulOrb.SoulOrbEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SurfWaveModel extends GeoModel<SurfWaveEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
    @Override
    public ResourceLocation getModelResource(SurfWaveEntity surfWaveEntity) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(SurfWaveEntity surfWaveEntity) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(SurfWaveEntity surfWaveEntity) {
        return animations;
    }
}
