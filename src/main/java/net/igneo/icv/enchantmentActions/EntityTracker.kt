package net.igneo.icv.enchantmentActions

import net.igneo.icv.Utils.getSlotForType
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.networking.packet.EntitySyncS2CPacket
import net.igneo.icv.networking.sendToPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity

interface EntityTracker {
    var child: ICVEntity?

    fun syncClientChild(player: ServerPlayer, entity: Entity?, manager: EnchantmentManager) {
        val iD = entity?.id ?: -1
        println(entity)
        println(iD)

        val slot = getSlotForType(player, manager.javaClass)

        sendToPlayer(EntitySyncS2CPacket(iD, slot), player)
    }
}
