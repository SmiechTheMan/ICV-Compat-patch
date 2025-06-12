package net.igneo.icv.networking.packet

import net.igneo.icv.Utils.syncClientEntity
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class EntitySyncS2CPacket {
    private val entityID: Int
    private val slot: Int

    constructor(entityID: Int, slot: Int) {
        this.entityID = entityID
        this.slot = slot
    }

    constructor(buf: FriendlyByteBuf) {
        this.entityID = buf.readInt()
        this.slot = buf.readInt()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeInt(entityID)
        buf.writeInt(slot)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            println("syncing")
            syncClientEntity(entityID, slot)
        }
        return true
    }
}
