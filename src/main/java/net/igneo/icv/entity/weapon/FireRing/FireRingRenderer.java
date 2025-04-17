package net.igneo.icv.entity.weapon.FireRing;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FireRingRenderer extends GeoEntityRenderer<FireRingEntity> {
    
    public FireRingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FireRingModel());
    }
}
