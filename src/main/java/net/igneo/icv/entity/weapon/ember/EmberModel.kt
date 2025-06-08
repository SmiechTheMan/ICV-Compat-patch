package net.igneo.icv.entity.weapon.ember

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class EmberModel : GeoModel<EmberEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")


    override fun getModelResource(cometEntity: EmberEntity?): ResourceLocation {
        return this.model
    }

    override fun getTextureResource(cometEntity: EmberEntity?): ResourceLocation {
        return this.texture
    }

    override fun getAnimationResource(cometEntity: EmberEntity?): ResourceLocation {
        return this.animations
    }
}
