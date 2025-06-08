package net.igneo.icv.entity.chestplate.soulSpider

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SoulSpiderRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<SoulSpiderEntity?>(renderManager, SoulSpiderModel())
