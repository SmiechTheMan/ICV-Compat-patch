package net.igneo.icv.entity.weapon.boostCharge

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class BoostChargeModel : GeoModel<BoostChargeEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json")


    override fun getModelResource(snakeBiteEntity: BoostChargeEntity?): ResourceLocation {
        return this.model
    }

    override fun getTextureResource(snakeBiteEntity: BoostChargeEntity?): ResourceLocation {
        return this.texture
    }

    override fun getAnimationResource(snakeBiteEntity: BoostChargeEntity?): ResourceLocation {
        return this.animations
    }
}
