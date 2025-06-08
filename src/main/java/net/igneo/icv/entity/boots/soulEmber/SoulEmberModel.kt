package net.igneo.icv.entity.boots.soulEmber

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class SoulEmberModel : GeoModel<SoulEmberEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")

    override fun getModelResource(surfWaveEntity: SoulEmberEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(surfWaveEntity: SoulEmberEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(surfWaveEntity: SoulEmberEntity?): ResourceLocation {
        return animations
    }
}
