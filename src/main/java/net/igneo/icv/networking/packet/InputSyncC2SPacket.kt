package net.igneo.icv.networking.packet

import net.igneo.icv.enchantmentActions.Input.Companion.getInput
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class InputSyncC2SPacket {
    private val iD: Int

    constructor(iD: Int) {
        this.iD = iD
    }

    constructor(buf: FriendlyByteBuf) {
        iD = buf.readInt()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeInt(iD)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            val player = context.sender
            player!!.serverLevel()
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent { enchVar: PlayerEnchantmentActions ->
                    enchVar.input = getInput(iD)
                }
        }
        return true
    }
}