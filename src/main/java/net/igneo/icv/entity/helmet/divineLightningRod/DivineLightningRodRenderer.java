package net.igneo.icv.entity.helmet.divineLightningRod;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DivineLightningRodRenderer extends GeoEntityRenderer<DivineLightningRodEntity> {
    public DivineLightningRodRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DivineLightningRodModel());
    }
}
