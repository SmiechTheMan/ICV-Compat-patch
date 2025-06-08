package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import net.igneo.icv.ICV
import net.igneo.icv.client.animation.EnchantAnimationPlayer
import net.igneo.icv.client.indicators.BlackHoleIndicator
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

class JudgementManager(player: Player?) :
    ArmorEnchantManager(EnchantType.LEGGINGS, 100, -10, false, player) {
    private var kicking = false
    private var kickTicks = 0
    private var kickPos: Vec3? = null
    private var judged: Entity? = null

    override fun activate() {
        active = true
        println("activating")
        if (player!!.level() is ClientLevel) {
            animator!!.setAnimation(
                EnchantAnimationPlayer(
                    PlayerAnimationRegistry.getAnimation(
                        ResourceLocation(
                            ICV.MOD_ID,
                            "judgement_jump"
                        )
                    )!!
                )
            )
        } else {
        }
        kickPos = player!!.position()
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = BlackHoleIndicator(this)

    override fun canUse(): Boolean {
        return true
    }

    override fun tick() {
        super.tick()
        if (active) {
            if (kicking && !player!!.onGround()) {
                if (kickTicks < 10) {
                    ++kickTicks
                    player!!.setPos(kickPos)
                    if (kickTicks == 10) {
                        val yaw = Math.toRadians(player!!.yRot.toDouble())
                        val x = -sin(yaw)
                        val y = 0.5
                        val z = cos(yaw)
                        var scale = 2.0

                        var flatDirection = Vec3(x * scale, y, z * scale)
                        judged!!.deltaMovement = flatDirection
                        scale = -0.5
                        flatDirection = Vec3(x * scale, y, z * scale)
                        player!!.deltaMovement = flatDirection
                    }
                }
            } else if (activeTicks > 24) {
                kickPos = player!!.position()
                for (entity in player!!.level().getEntities(null, player!!.boundingBox.inflate(1.2))) {
                    if (entity !== player) {
                        kicking = true
                        judged = entity
                        if (player!!.level().isClientSide) {
                            animator!!.setAnimation(
                                EnchantAnimationPlayer(
                                    PlayerAnimationRegistry.getAnimation(
                                        ResourceLocation(ICV.MOD_ID, "judgement_hit")
                                    )!!
                                )
                            )
                        } else {
                        }
                    }
                }
            }
            if (activeTicks == 24) {
                val yaw = Math.toRadians(player!!.yRot.toDouble())
                val x = -sin(yaw)
                val y = 0.35
                val z = cos(yaw)
                val scale = 3.5

                val flatDirection = Vec3(x * scale, y, z * scale)
                player!!.deltaMovement = flatDirection
            } else if (activeTicks < 24) {
                if (kickPos != null) player!!.setPos(kickPos)
            }
            if (player!!.level().isClientSide) {
                if (!animator!!.isActive) {
                    animator!!.animation =
                        EnchantAnimationPlayer(
                            PlayerAnimationRegistry.getAnimation(
                                ResourceLocation(
                                    ICV.MOD_ID,
                                    "judgement_idle"
                                )
                            )!!
                        )
                }
            }
            if (player!!.onGround() && activeTicks > 28 && !(kicking && kickTicks < 10)) {
                if (!kicking) {
                    if (player!!.level().isClientSide) {
                        animator!!.animation =
                            EnchantAnimationPlayer(
                                PlayerAnimationRegistry.getAnimation(
                                    ResourceLocation(
                                        ICV.MOD_ID,
                                        "judgement_miss"
                                    )
                                )!!
                            )
                    }
                    val yaw = Math.toRadians(player!!.yRot.toDouble())
                    val x = -sin(yaw)
                    val y = 0.0
                    val z = cos(yaw)
                    val scale = 2.5

                    val flatDirection = Vec3(x * scale, y, z * scale)
                    player!!.deltaMovement = flatDirection
                }
                activeTicks = 0
                active = false
                kicking = false
                kickPos = null
                kickTicks = 0
            }
        }
    }
}
