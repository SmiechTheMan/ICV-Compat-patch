package net.igneo.icv.entity.meteorSummoner;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MeteorSummonerModel extends GeoModel<MeteorSummonerEntity> {
  private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
  private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
  private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
  @Override
  public ResourceLocation getModelResource(MeteorSummonerEntity abyssStoneEntity) {
    return model;
  }
  
  @Override
  public ResourceLocation getTextureResource(MeteorSummonerEntity abyssStoneEntity) {
    return texture;
  }
  
  @Override
  public ResourceLocation getAnimationResource(MeteorSummonerEntity abyssStoneEntity) {
    return animations;
  }
}
