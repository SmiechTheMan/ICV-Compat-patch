package net.igneo.icv.enchantmentActions.enchantManagers.trident

import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.init.ICVUtils
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.PushPlayerS2CPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.phys.BlockHitResult

class WhirlpoolManager(player: Player?) :
    TridentEnchantManager(EnchantType.TRIDENT, player) {
    private var canPull = true

    override fun onHitBlock(result: BlockHitResult?, trident: ThrownTrident?) {
        doPull(trident!!)
    }


    private fun doPull(trident: ThrownTrident) {
        if (!canPull) {
            return
        }
        for (entity in ICVUtils.collectEntitiesBox(player.level(), trident.position(), 3.5)) {
            if (entity === trident || entity === player) {
                continue
            }
            if (entity is ServerPlayer) {
                val pullVector = ICVUtils.DOWN_VECTOR
                entity.addDeltaMovement(pullVector)
                ModMessages.sendToPlayer(PushPlayerS2CPacket(ICVUtils.DOWN_VECTOR), entity)
            } else {
                entity.addDeltaMovement(ICVUtils.DOWN_VECTOR)
            }
        }
        canPull = false
    }
}
