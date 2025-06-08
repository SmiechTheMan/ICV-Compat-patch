package net.igneo.icv.entity.chestplate.soulOrb

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SoulOrbRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<SoulOrbEntity?>(renderManager, SoulOrbModel())
