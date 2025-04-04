package net.igneo.icv.entity.weapon.ember;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class EmberRenderer extends GeoEntityRenderer<EmberEntity> {
    public EmberRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmberModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
