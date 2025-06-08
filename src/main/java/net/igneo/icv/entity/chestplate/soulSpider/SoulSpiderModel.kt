package net.igneo.icv.entity.chestplate.soulSpider

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class SoulSpiderModel : GeoModel<SoulSpiderEntity?>() {
    private val model = ResourceLocation(ICV.MOD_ID, "geo/soul_spider.geo.json")
    private val texture = ResourceLocation(ICV.MOD_ID, "textures/entity/soul_spider.png")
    private val animations = ResourceLocation(ICV.MOD_ID, "animations/soul_spider.animation.json")


    override fun getModelResource(soulSpiderEntity: SoulSpiderEntity?): ResourceLocation {
        return this.model
    }

    override fun getTextureResource(soulSpiderEntity: SoulSpiderEntity?): ResourceLocation {
        return this.texture
    }

    override fun getAnimationResource(soulSpiderEntity: SoulSpiderEntity?): ResourceLocation {
        return this.animations
    }
}
