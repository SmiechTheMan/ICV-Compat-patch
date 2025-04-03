package net.igneo.icv.entity.divineLightningRod;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DivineLightningRodModel extends GeoModel<DivineLightningRodEntity> {
  private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
  private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
  private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
  @Override
  public ResourceLocation getModelResource(DivineLightningRodEntity animatable) {
    return model;
  }
  
  @Override
  public ResourceLocation getTextureResource(DivineLightningRodEntity animatable) {
    return texture;
  }
  
  @Override
  public ResourceLocation getAnimationResource(DivineLightningRodEntity animatable) {
    return animations;
  }
}
