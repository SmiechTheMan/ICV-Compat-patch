package net.igneo.icv.entity.boots.surfWave;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SurfWaveRenderer extends GeoEntityRenderer<SurfWaveEntity> {
    public SurfWaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SurfWaveModel());
    }
}
