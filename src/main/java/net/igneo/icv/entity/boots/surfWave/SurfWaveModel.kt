package net.igneo.icv.entity.boots.surfWave

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class SurfWaveModel : GeoModel<SurfWaveEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/surf.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/surf.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/surf.animation.json")

    override fun getModelResource(surfWaveEntity: SurfWaveEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(surfWaveEntity: SurfWaveEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(surfWaveEntity: SurfWaveEntity?): ResourceLocation {
        return animations
    }
}
