package net.igneo.icv.entity.voidSpike;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VoidSpikeRenderer extends GeoEntityRenderer<VoidSpikeEntity> {
    public VoidSpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VoidSpikeModel());
    }
}
