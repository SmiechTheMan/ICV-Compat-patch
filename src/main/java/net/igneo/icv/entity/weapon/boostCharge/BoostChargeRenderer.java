package net.igneo.icv.entity.weapon.boostCharge;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoostChargeRenderer extends GeoEntityRenderer<BoostChargeEntity> {
    
    public BoostChargeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoostChargeModel());
    }
}
