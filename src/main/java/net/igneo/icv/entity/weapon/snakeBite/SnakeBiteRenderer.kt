package net.igneo.icv.entity.weapon.snakeBite

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SnakeBiteRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<SnakeBiteEntity?>(renderManager, SnakeBiteModel())
