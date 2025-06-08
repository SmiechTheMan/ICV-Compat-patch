package net.igneo.icv.entity.chestplate.meteorSummoner

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class MeteorSummonerModel : GeoModel<MeteorSummonerEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")

    override fun getModelResource(abyssStoneEntity: MeteorSummonerEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(abyssStoneEntity: MeteorSummonerEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(abyssStoneEntity: MeteorSummonerEntity?): ResourceLocation {
        return animations
    }
}
