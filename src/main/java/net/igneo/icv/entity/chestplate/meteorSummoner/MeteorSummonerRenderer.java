package net.igneo.icv.entity.chestplate.meteorSummoner;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MeteorSummonerRenderer extends GeoEntityRenderer<MeteorSummonerEntity> {
  public MeteorSummonerRenderer(EntityRendererProvider.Context renderManager) {
    super(renderManager, new MeteorSummonerModel());
  }
}
