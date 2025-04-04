package net.igneo.icv.entity.boots.soulEmber;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulEmberRenderer extends GeoEntityRenderer<SoulEmberEntity> {
    public SoulEmberRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SoulEmberModel());
    }
}
