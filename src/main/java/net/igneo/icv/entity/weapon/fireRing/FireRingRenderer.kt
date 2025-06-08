package net.igneo.icv.entity.weapon.fireRing

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class FireRingRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<FireRingEntity?>(renderManager, FireRingModel())
