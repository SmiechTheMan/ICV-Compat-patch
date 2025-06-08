package net.igneo.icv.shader.shader

import net.igneo.icv.shader.postProcessors.BlinkPostProcessor
import org.joml.Vector3f
import team.lodestar.lodestone.systems.postprocess.DynamicShaderFxInstance
import java.util.function.BiConsumer

class BlinkFx(var center: Vector3f, var color: Vector3f) : DynamicShaderFxInstance() {
    var instTime: Float = 0.0f
    var storedTime: Long = 0
    var deltaTime: Double = 0.0
    var parent: BlinkPostProcessor? = null

    init {
        this.instTime = 0f
    }

    override fun update(deltaTime: Double) {
        if (this.instTime * 2 >= 1) {
            this.remove()
        }
        this.deltaTime = ((System.nanoTime() - storedTime) / 1000000000.0f).toDouble()
        storedTime = System.nanoTime()
        this.instTime = (instTime.toDouble() + this.deltaTime).toFloat()
    }

    override fun writeDataToBuffer(writer: BiConsumer<Int, Float>) {
        writer.accept(0, center.x())
        writer.accept(1, center.y())
        writer.accept(2, center.z())
        writer.accept(3, color.x())
        writer.accept(4, color.y())
        writer.accept(5, color.z())
        writer.accept(6, instTime)
    }
}
