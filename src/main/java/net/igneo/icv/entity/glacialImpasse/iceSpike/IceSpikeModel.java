package net.igneo.icv.entity.glacialImpasse.iceSpike;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IceSpikeModel extends GeoModel<IceSpikeEntity> {
  private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
  private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
  private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
  @Override
  public ResourceLocation getModelResource(IceSpikeEntity animatable) {
    return model;
  }
  
  @Override
  public ResourceLocation getTextureResource(IceSpikeEntity animatable) {
    return texture;
  }
  
  @Override
  public ResourceLocation getAnimationResource(IceSpikeEntity animatable) {
    return animations;
  }
}
