package net.igneo.icv.enchantmentActions

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.init.ICVUtils
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.EntitySyncS2CPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity

interface EntityTracker {
    var child: ICVEntity?

    fun syncClientChild(player: ServerPlayer, entity: Entity?, manager: EnchantmentManager) {
        val iD = entity?.id ?: -1
        println(entity)
        println(iD)

        val slot = ICVUtils.getSlotForType(player, manager.javaClass)

        ModMessages.sendToPlayer(EntitySyncS2CPacket(iD, slot), player)
    }
}
