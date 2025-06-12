package net.igneo.icv.enchantmentActions.enchantManagers.trident

import net.igneo.icv.Utils.DOWN_VECTOR
import net.igneo.icv.Utils.collectEntitiesBox
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.networking.packet.PushPlayerS2CPacket
import net.igneo.icv.networking.sendToPlayer
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
        for (entity in collectEntitiesBox(player.level(), trident.position(), 3.5)) {
            if (entity === trident || entity === player) {
                continue
            }
            val pullVector = DOWN_VECTOR
            if (entity is ServerPlayer) {
                entity.addDeltaMovement(pullVector)
                sendToPlayer(PushPlayerS2CPacket(pullVector), entity)
            } else {
                entity.addDeltaMovement(pullVector)
            }
        }
        canPull = false
    }
}
