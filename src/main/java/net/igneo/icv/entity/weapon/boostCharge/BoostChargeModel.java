package net.igneo.icv.entity.weapon.boostCharge;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoostChargeModel extends GeoModel<BoostChargeEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
    
    
    @Override
    public ResourceLocation getModelResource(BoostChargeEntity snakeBiteEntity) {
        return this.model;
    }
    
    @Override
    public ResourceLocation getTextureResource(BoostChargeEntity snakeBiteEntity) {
        return this.texture;
    }
    
    @Override
    public ResourceLocation getAnimationResource(BoostChargeEntity snakeBiteEntity) {
        return this.animations;
    }
}
