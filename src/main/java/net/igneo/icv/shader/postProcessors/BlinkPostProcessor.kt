package net.igneo.icv.shader.postProcessors

import com.mojang.blaze3d.vertex.PoseStack
import net.igneo.icv.ICV
import net.igneo.icv.shader.shader.BlinkFx
import net.minecraft.client.renderer.EffectInstance
import net.minecraft.resources.ResourceLocation
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor

class BlinkPostProcessor : MultiInstancePostProcessor<BlinkFx?>() {
    private var effectBlink: EffectInstance? = null

    private val instances = ArrayList<BlinkFx?>()

    override fun getPostChainLocation(): ResourceLocation {
        return ResourceLocation(ICV.MOD_ID, "tint_post")
    }

    // Max amount of FxInstances that can be added to the post processor at once
    override fun getMaxInstances(): Int {
        return 16
    }

    // We passed in a total of 6 floats/uniforms to the shader inside our LightingFx class so this should return 6, will crash if it doesn't match
    override fun getDataSizePerInstance(): Int {
        return 7
    }

    override fun init() {
        super.init()
        if (postChain != null) {
            effectBlink = effects[0]
        }
    }

    override fun addFxInstance(instance: BlinkFx?): BlinkFx? {
        instances.add(instance)
        return super.addFxInstance(instance)
    }

    override fun beforeProcess(viewModelStack: PoseStack) {
        super.beforeProcess(viewModelStack)
        setDataBufferUniform(effectBlink, "DataBuffer", "InstanceCount")
    }

    override fun afterProcess() {
    }

    companion object {
        @JvmField
        val INSTANCE: BlinkPostProcessor = BlinkPostProcessor()
    }
}
