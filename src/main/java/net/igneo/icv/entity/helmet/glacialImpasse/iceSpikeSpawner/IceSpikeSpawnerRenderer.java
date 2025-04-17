package net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceSpikeSpawnerRenderer extends GeoEntityRenderer<IceSpikeSpawnerEntity> {
    public IceSpikeSpawnerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new iceSpikeSpawnerModel());
    }
}
