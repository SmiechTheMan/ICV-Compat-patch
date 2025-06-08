package net.igneo.icv.entity.weapon.boostCharge

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class BoostChargeRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<BoostChargeEntity?>(renderManager, BoostChargeModel())
