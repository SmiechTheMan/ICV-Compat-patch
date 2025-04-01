package net.igneo.icv.entity.abyssStone;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AbyssStoneModel extends GeoModel<AbyssStoneEntity> {
  private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
  private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
  private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
  @Override
  public ResourceLocation getModelResource(AbyssStoneEntity abyssStoneEntity) {
    return model;
  }
  
  @Override
  public ResourceLocation getTextureResource(AbyssStoneEntity abyssStoneEntity) {
    return texture;
  }
  
  @Override
  public ResourceLocation getAnimationResource(AbyssStoneEntity abyssStoneEntity) {
    return animations;
  }
}
