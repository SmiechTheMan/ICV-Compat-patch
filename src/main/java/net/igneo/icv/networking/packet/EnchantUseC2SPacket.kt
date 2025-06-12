package net.igneo.icv.networking.packet

import net.igneo.icv.Utils.useEnchant
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class EnchantUseC2SPacket {
    private val slot: Int

    constructor(slot: Int) {
        this.slot = slot
    }

    constructor(buf: FriendlyByteBuf) {
        this.slot = buf.readInt()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeInt(this.slot)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            context.sender!!.serverLevel()
            val player = context.sender
            println("using on the server")
            useEnchant(player!!, slot)
        }
        return true
    }
}
