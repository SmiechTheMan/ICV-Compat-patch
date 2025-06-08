package net.igneo.icv.entity.chestplate.abyssStone

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class AbyssStoneRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<AbyssStoneEntity?>(renderManager, AbyssStoneModel())
