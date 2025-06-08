package net.igneo.icv.networking.packet

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class AnimatedSyncC2SPacket {
    private val state: Boolean

    constructor(state: Boolean) {
        this.state = state
    }

    constructor(buf: FriendlyByteBuf) {
        state = buf.readBoolean()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeBoolean(state)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            val player = context.sender
            player!!.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent { enchVar: PlayerEnchantmentActions -> enchVar.animated = state }
        }
        return true
    }
}