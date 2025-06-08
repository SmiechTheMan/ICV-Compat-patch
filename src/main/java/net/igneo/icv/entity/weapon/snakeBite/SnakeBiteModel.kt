package net.igneo.icv.entity.weapon.snakeBite

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class SnakeBiteModel : GeoModel<SnakeBiteEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")


    override fun getModelResource(snakeBiteEntity: SnakeBiteEntity?): ResourceLocation {
        return this.model
    }

    override fun getTextureResource(snakeBiteEntity: SnakeBiteEntity?): ResourceLocation {
        return this.texture
    }

    override fun getAnimationResource(snakeBiteEntity: SnakeBiteEntity?): ResourceLocation {
        return this.animations
    }
}
