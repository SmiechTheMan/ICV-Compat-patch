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
import net.minecraft.world.phys.Vec3

class GeyserManager(player: Player?) :
    TridentEnchantManager(EnchantType.TRIDENT, player) {
    private var canPush = true

    override fun onHitBlock(result: BlockHitResult?, trident: ThrownTrident?) {
        doPush(trident)
    }

    override fun onHitEntity(result: EntityHitResult?, trident: ThrownTrident?) {
        if (result!!.entity.onGround()) {
            doPush(trident)
        }
    }

    override fun tick() {
        super.tick()
        if (trident == null) {
            canPush = true
        } else if (trident!!.onGround() || trident!!.deltaMovement.length() < 0.1) {
            doPush(trident!!)
        }
    }

    private fun doPush(trident: ThrownTrident?) {
        if (canPush) {
            for (entity in ICVUtils.collectEntitiesBox(player.level(), trident!!.position(), 3.5)) {
                if (entity !== trident) {
                    if (entity is ServerPlayer) {
                        println("pushing the player")
                        entity.addDeltaMovement(Vec3(0.0, 1.0, 0.0))
                        ModMessages.sendToPlayer(PushPlayerS2CPacket(Vec3(0.0, 1.0, 0.0)), entity)
                    } else {
                        entity.addDeltaMovement(Vec3(0.0, 1.0, 0.0))
                    }
                }
            }
            canPush = false
        }
    }
}
