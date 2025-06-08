package net.igneo.icv.entity.boots.stonePillar

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class StonePillarRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<StonePillarEntity?>(renderManager, StonePillarModel())
