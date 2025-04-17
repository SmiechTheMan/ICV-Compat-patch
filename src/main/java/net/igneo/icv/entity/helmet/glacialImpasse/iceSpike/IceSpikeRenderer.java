package net.igneo.icv.entity.helmet.glacialImpasse.iceSpike;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceSpikeRenderer extends GeoEntityRenderer<IceSpikeEntity> {
    public IceSpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IceSpikeModel());
    }
}
