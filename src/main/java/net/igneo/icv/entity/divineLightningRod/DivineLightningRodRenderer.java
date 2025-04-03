package net.igneo.icv.entity.divineLightningRod;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DivineLightningRodRenderer extends GeoEntityRenderer<DivineLightningRodEntity> {
  public DivineLightningRodRenderer(EntityRendererProvider.Context renderManager) {
    super(renderManager, new DivineLightningRodModel());
  }
}
