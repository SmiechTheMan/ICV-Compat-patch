package net.igneo.icv.entity.weapon.FireRing;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FireRingModel extends GeoModel<FireRingEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
    
    
    @Override
    public ResourceLocation getModelResource(FireRingEntity snakeBiteEntity) {
        return this.model;
    }
    
    @Override
    public ResourceLocation getTextureResource(FireRingEntity snakeBiteEntity) {
        return this.texture;
    }
    
    @Override
    public ResourceLocation getAnimationResource(FireRingEntity snakeBiteEntity) {
        return this.animations;
    }
}
