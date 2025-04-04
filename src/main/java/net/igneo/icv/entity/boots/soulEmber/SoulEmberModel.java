package net.igneo.icv.entity.boots.soulEmber;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulEmberModel extends GeoModel<SoulEmberEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
    @Override
    public ResourceLocation getModelResource(SoulEmberEntity surfWaveEntity) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(SoulEmberEntity surfWaveEntity) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(SoulEmberEntity surfWaveEntity) {
        return animations;
    }
}
