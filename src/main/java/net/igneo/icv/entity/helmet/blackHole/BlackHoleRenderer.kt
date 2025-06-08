package net.igneo.icv.entity.helmet.blackHole

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class BlackHoleRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<BlackHoleEntity?>(renderManager, BlackHoleModel())
