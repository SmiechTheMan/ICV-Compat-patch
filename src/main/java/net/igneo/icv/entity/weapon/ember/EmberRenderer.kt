package net.igneo.icv.entity.weapon.ember

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer

class EmberRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<EmberEntity?>(renderManager, EmberModel()) {
    init {
        addRenderLayer(AutoGlowingGeoLayer(this))
    }
}
