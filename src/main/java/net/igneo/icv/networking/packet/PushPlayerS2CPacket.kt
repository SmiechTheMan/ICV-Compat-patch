package net.igneo.icv.networking.packet

import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class PushPlayerS2CPacket {
    private val x: Double
    private val y: Double
    private val z: Double

    constructor(addDirection: Vec3) {
        this.x = addDirection.x
        this.y = addDirection.y
        this.z = addDirection.z
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
            val pushVec = Vec3(x, y, z)
            Minecraft.getInstance().player!!.addDeltaMovement(pushVec)
        }
        return true
    }
}
