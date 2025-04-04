package net.igneo.icv.entity.chestplate.soulOrb;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulOrbRenderer extends GeoEntityRenderer<SoulOrbEntity> {

    public SoulOrbRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SoulOrbModel());
    }
}
