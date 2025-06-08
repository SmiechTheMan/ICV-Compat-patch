package net.igneo.icv.entity.helmet.glacialImpasse.iceSpike

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class IceSpikeRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<IceSpikeEntity?>(renderManager, IceSpikeModel())
