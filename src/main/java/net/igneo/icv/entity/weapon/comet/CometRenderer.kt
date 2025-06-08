package net.igneo.icv.entity.weapon.comet

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer

class CometRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<CometEntity?>(renderManager, CometModel()) {
    init {
        addRenderLayer(AutoGlowingGeoLayer(this))
    }
}
