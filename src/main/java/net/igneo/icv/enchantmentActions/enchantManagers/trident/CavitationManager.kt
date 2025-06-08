package net.igneo.icv.enchantmentActions.enchantManagers.trident

import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.init.ICVUtils
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.PushPlayerS2CPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class CavitationManager(player: Player?) :
    TridentEnchantManager(EnchantType.TRIDENT, player) {
    override fun onHitBlock(result: BlockHitResult?, trident: ThrownTrident?) {
        pullTowards(trident)
    }

    override fun onHitEntity(result: EntityHitResult?, trident: ThrownTrident?) {
        pullTowards(trident)
    }

    private fun pullTowards(trident: ThrownTrident?) {
        val nearybyEntities = ICVUtils.collectEntitiesBox(player!!.level(), trident!!.position(), 3.5)

        for (entity in nearybyEntities) {
            if (entity === player) {
                continue
            }
            val direction = trident.position().subtract(entity.position()).normalize()

            val pullVeloctiy = direction.scale(1.5)
            entity.addDeltaMovement(pullVeloctiy)
            entity.hurtMarked = true

            if (entity is ServerPlayer) {
                ModMessages.sendToPlayer(PushPlayerS2CPacket(pullVeloctiy), player as ServerPlayer)
            }
        }
    }
}
