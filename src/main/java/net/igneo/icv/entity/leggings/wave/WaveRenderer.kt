package net.igneo.icv.entity.leggings.wave

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class WaveRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<WaveEntity?>(renderManager, WaveModel())
