package net.igneo.icv.shader

import net.igneo.icv.shader.postProcessors.BlinkPostProcessor
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler

object ModShaders {
    private fun registerShader(shader: MultiInstancePostProcessor<*>) {
        PostProcessHandler.addInstance(shader)
    }

    fun register() {
        registerShader(BlinkPostProcessor.INSTANCE)
    }
}
