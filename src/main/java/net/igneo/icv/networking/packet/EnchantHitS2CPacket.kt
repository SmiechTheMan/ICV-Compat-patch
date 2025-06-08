package net.igneo.icv.networking.packet

import net.igneo.icv.init.ICVUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class EnchantHitS2CPacket {

    constructor()

    constructor(buf: FriendlyByteBuf)

    fun toBytes(buf: FriendlyByteBuf) {
        // Empty implementation - no data to serialize
    }

    fun handle(supplier: Supplier<NetworkEvent.Context?>?): Boolean {
        val context = supplier!!.get()
        context?.enqueueWork { ICVUtils.clientCooldownDamageBonuses() }
        return true
    }
}