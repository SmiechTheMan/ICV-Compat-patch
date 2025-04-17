package net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class iceSpikeSpawnerModel extends GeoModel<IceSpikeSpawnerEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
    
    @Override
    public ResourceLocation getModelResource(IceSpikeSpawnerEntity animatable) {
        return model;
    }
    
    @Override
    public ResourceLocation getTextureResource(IceSpikeSpawnerEntity animatable) {
        return texture;
    }
    
    @Override
    public ResourceLocation getAnimationResource(IceSpikeSpawnerEntity animatable) {
        return animations;
    }
}
