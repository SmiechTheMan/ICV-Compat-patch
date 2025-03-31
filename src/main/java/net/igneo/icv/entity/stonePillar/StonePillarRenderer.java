package net.igneo.icv.entity.stonePillar;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StonePillarRenderer extends GeoEntityRenderer<StonePillarEntity> {
    public StonePillarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StonePillarModel());
    }
}
