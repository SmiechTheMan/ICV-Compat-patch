package net.igneo.icv.entity.weapon.ember;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EmberModel extends GeoModel<EmberEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");


    @Override
    public ResourceLocation getModelResource(EmberEntity cometEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(EmberEntity cometEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(EmberEntity cometEntity) {
        return this.animations;
    }
}
