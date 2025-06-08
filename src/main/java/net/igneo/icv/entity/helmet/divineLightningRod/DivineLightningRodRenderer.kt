package net.igneo.icv.entity.helmet.divineLightningRod

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class DivineLightningRodRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<DivineLightningRodEntity?>(renderManager, DivineLightningRodModel())
