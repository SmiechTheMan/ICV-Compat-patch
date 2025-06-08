package net.igneo.icv.entity.leggings.voidSpike

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class VoidSpikeModel : GeoModel<VoidSpikeEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")

    override fun getModelResource(surfWaveEntity: VoidSpikeEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(surfWaveEntity: VoidSpikeEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(surfWaveEntity: VoidSpikeEntity?): ResourceLocation {
        return animations
    }
}
