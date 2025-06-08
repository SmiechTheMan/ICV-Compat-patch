package net.igneo.icv.networking.packet

import net.igneo.icv.shader.postProcessors.BlinkPostProcessor
import net.igneo.icv.shader.shader.BlinkFx
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkEvent
import org.joml.Vector3f
import java.util.function.Supplier

class SendBlinkShaderS2CPacket {
    private val x: Double
    private val y: Double
    private val z: Double

    constructor(pos: Vec3) {
        this.x = pos.x
        this.y = pos.y
        this.z = pos.z
    }

    constructor(buf: FriendlyByteBuf) {
        this.x = buf.readDouble()
        this.y = buf.readDouble()
        this.z = buf.readDouble()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            val pos = Vec3(x, y, z)
            val center = pos.toVector3f()
            val color = Vector3f(0.7f, 0.0f, 1f)
            val fx = BlinkFx(center, color)
            fx.instTime = 0f
            fx.storedTime = System.nanoTime()
            fx.deltaTime = ((System.nanoTime() - fx.storedTime) / 1000000000.0f).toDouble()
            BlinkPostProcessor.INSTANCE.addFxInstance(fx)
        }
        return true
    }
}
