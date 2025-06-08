package net.igneo.icv.entity.chestplate.meteorSummoner

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class MeteorSummonerRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<MeteorSummonerEntity?>(renderManager, MeteorSummonerModel())
