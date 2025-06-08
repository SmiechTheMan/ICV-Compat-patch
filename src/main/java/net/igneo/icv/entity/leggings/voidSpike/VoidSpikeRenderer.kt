package net.igneo.icv.entity.leggings.voidSpike

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class VoidSpikeRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<VoidSpikeEntity?>(renderManager, VoidSpikeModel())
