package net.igneo.icv.enchantmentActions.enchantManagers.trident

import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.MovePlayerS2CPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

class UndertowManager(player: Player?) :
    TridentEnchantManager(EnchantType.TRIDENT, player) {
    private var realeased = false

    override fun onHitBlock(result: BlockHitResult?, trident: ThrownTrident?) {
        println("PAY ATTENTION")
        active = true
    }

    override fun onHitEntity(result: EntityHitResult?, trident: ThrownTrident?) {
        println("PAY ATTENTION ENTITY")
        active = true
    }

    override fun onRelease() {
        println("PAY ATTENTION RELEASE")
        realeased = true
    }

    override fun tick() {
        super.tick()
        if (trident == null) {
            return
        }
        if (!trident!!.onGround()) {
            return
        }
        if (active && realeased) {
            println(
                """
                    $active
                    $realeased
                    """.trimIndent()
            )
        }
        pullPlayer(trident!!)
    }

    private fun pullPlayer(trident: ThrownTrident) {
        val xDiff = trident.x - player!!.x
        val yDiff = trident.y - player!!.y
        val zDiff = trident.z - player!!.z

        val distance = sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff)

        val playerMovement = Vec3(
            player!!.deltaMovement.x + (xDiff / distance),
            player!!.deltaMovement.y + (yDiff / distance),
            player!!.deltaMovement.z + (zDiff / distance)
        )

        if (distance < 3.0) {
            active = false
            realeased = false
            return
        }

        player!!.deltaMovement = playerMovement

        player!!.fallDistance = 0.0f

        if (player is ServerPlayer) {
            ModMessages.sendToPlayer(
                MovePlayerS2CPacket(Vec3(playerMovement.x, playerMovement.y, playerMovement.z)),
                player as ServerPlayer
            )
        }
    }
}