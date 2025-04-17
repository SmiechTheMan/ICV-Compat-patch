package net.igneo.icv.entity.boots.surfWave;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SurfWaveModel extends GeoModel<SurfWaveEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/surf.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/surf.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/surf.animation.json");
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
