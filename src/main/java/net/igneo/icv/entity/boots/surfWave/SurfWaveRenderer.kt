package net.igneo.icv.entity.boots.surfWave

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer

class SurfWaveRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<SurfWaveEntity>(renderManager, SurfWaveModel()) {
    override fun actuallyRender(
        poseStack: PoseStack,
        animatable: SurfWaveEntity,
        model: BakedGeoModel,
        renderType: RenderType,
        bufferSource: MultiBufferSource,
        buffer: VertexConsumer,
        isReRender: Boolean,
        partialTick: Float,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        this.applyRotations(
            animatable,
            poseStack,
            animatable.tickCount.toFloat() + partialTick,
            Mth.rotLerp(partialTick, animatable.yRotO, animatable.yRot),
            partialTick
        )

        super.actuallyRender(
            poseStack,
            animatable,
            model,
            renderType,
            bufferSource,
            buffer,
            isReRender,
            partialTick,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
        )
    }
}
