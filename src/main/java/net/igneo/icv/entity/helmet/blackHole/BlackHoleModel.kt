package net.igneo.icv.entity.helmet.blackHole

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class BlackHoleModel : GeoModel<BlackHoleEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/blackhole.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/blackhole.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/blackhole.animation.json")


    override fun getModelResource(blackHoleEntity: BlackHoleEntity?): ResourceLocation {
        return this.model
    }

    override fun getTextureResource(blackHoleEntity: BlackHoleEntity?): ResourceLocation {
        return this.texture
    }

    override fun getAnimationResource(blackHoleEntity: BlackHoleEntity?): ResourceLocation {
        return this.animations
    }
}
