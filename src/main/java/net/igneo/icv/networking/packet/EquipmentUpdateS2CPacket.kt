package net.igneo.icv.networking.packet

import net.igneo.icv.Utils.directUpdate
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class EquipmentUpdateS2CPacket {
    private val slot: Int

    constructor(slot: Int) {
        this.slot = slot
    }

    constructor(buf: FriendlyByteBuf) {
        this.slot = buf.readInt()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeInt(slot)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            directUpdate(slot)
        }
        return true
    }
}
