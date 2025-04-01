package net.igneo.icv.entity.wave;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaveRenderer extends GeoEntityRenderer<WaveEntity> {
    public WaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WaveModel());
    }
}
