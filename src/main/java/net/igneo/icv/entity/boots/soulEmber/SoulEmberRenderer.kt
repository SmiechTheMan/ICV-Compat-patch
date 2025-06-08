package net.igneo.icv.entity.boots.soulEmber

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SoulEmberRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<SoulEmberEntity?>(renderManager, SoulEmberModel())
