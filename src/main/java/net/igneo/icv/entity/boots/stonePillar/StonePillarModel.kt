package net.igneo.icv.entity.boots.stonePillar

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class StonePillarModel : GeoModel<StonePillarEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")

    override fun getModelResource(surfWaveEntity: StonePillarEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(surfWaveEntity: StonePillarEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(surfWaveEntity: StonePillarEntity?): ResourceLocation {
        return animations
    }
}
