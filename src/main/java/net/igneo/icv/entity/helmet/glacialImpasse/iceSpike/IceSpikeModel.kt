package net.igneo.icv.entity.helmet.glacialImpasse.iceSpike

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class IceSpikeModel : GeoModel<IceSpikeEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")

    override fun getModelResource(animatable: IceSpikeEntity?): ResourceLocation {
        return model
    }

    override fun getTextureResource(animatable: IceSpikeEntity?): ResourceLocation {
        return texture
    }

    override fun getAnimationResource(animatable: IceSpikeEntity?): ResourceLocation {
        return animations
    }
}
