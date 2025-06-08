package net.igneo.icv.networking.packet

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class EnchantAttackS2CPacket {
    private val slot: Int
    private val iD: Int

    constructor(slot: Int, iD: Int) {
        this.slot = slot
        this.iD = iD
    }

    constructor(buf: FriendlyByteBuf) {
        this.slot = buf.readInt()
        this.iD = buf.readInt()
    }

    fun toBytes(buf: FriendlyByteBuf) {
        buf.writeInt(slot)
        buf.writeInt(iD)
    }

    fun handle(supplier: Supplier<NetworkEvent.Context>): Boolean {
        val context = supplier.get()
        context.enqueueWork {
            Minecraft.getInstance().player!!.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent { enchVar: PlayerEnchantmentActions ->
                    if (slot == 5) {
                        (enchVar.getManager(5) as WeaponEnchantManager).onAttack(
                            Minecraft.getInstance().player!!.level()
                                .getEntity(iD)
                        )
                    } else if (slot == 4) {
                        (enchVar.getManager(4) as WeaponEnchantManager).onAttack(
                            Minecraft.getInstance().player!!.level()
                                .getEntity(iD)
                        )
                    }
                }
        }
        return true
    }
}
