package net.igneo.icv.networking.packet

import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class MovePlayerS2CPacket {
    private val x: Double
    private val y: Double
    private val z: Double

    constructor(setDirection: Vec3) {
        this.x = setDirection.x
        this.y = setDirection.y
        this.z = setDirection.z
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
            val x =
                if ((this.x == 0.0)) Minecraft.getInstance().player!!.deltaMovement.x else x
            val z =
                if ((this.z == 0.0)) Minecraft.getInstance().player!!.deltaMovement.z else z
            val setVec = Vec3(x, y, z)
            Minecraft.getInstance().player!!.deltaMovement = setVec
        }
        return true
    }
}
