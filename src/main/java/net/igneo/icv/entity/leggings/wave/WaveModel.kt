package net.igneo.icv.entity.leggings.wave

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class WaveModel : GeoModel<WaveEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")

    override fun getModelResource(surfWaveEntity: WaveEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(surfWaveEntity: WaveEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(surfWaveEntity: WaveEntity?): ResourceLocation {
        return animations
    }
}
