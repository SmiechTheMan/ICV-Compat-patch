package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.sound.ModSounds
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin

class BurstManager(player: Player?) :
    WeaponEnchantManager(
        EnchantType.WEAPON,
        player!!,
        ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")
    ) {
    private var burstBoostCount = 0
    private var burstTimer = 0

    override fun onAttack(entity: Entity?) {
        super.onAttack(target)
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent {
                if (player.fallDistance <= 0) {
                    ++burstBoostCount
                } else {
                    ++burstBoostCount
                    ++burstBoostCount
                    ++burstBoostCount
                }
                burstTimer = 0
                val level = target!!.level()
                if (target!!.level() is ServerLevel) {
                    level.playSound(
                        null, player.blockPosition(),
                        SoundEvents.ARROW_HIT_PLAYER,
                        SoundSource.PLAYERS,
                        0.5f, 0.3.toFloat() + burstBoostCount.toFloat() * 0.1f
                    )
                    player.attributes.getInstance(Attributes.ATTACK_SPEED)!!
                        .removeModifier(ATTACK_SPEED_MODIFIER_UUID)
                    player.attributes.getInstance(Attributes.ATTACK_SPEED)!!
                        .addTransientModifier(
                            AttributeModifier(
                                ATTACK_SPEED_MODIFIER_UUID,
                                "Attack Speed Boost Burst",
                                (burstBoostCount * 0.1f).toDouble(),
                                AttributeModifier.Operation.ADDITION
                            )
                        )
                }
            }
    }

    override fun activate() {
        super.activate()


        val level = target!!.level()
        if (player.level() is ServerLevel) {
            level.playSound(null, player.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS, 0.5f, 1.2f)

            val radius = 2.0
            val pushStrength = 2.5f


            val burstBoostDecay = ln(burstBoostCount.toDouble()).toFloat()

            renderRadius(level as ServerLevel, player.position(), radius * burstBoostDecay)

            val nearbyEntities: List<Entity> = level.getEntities(player,
                player.boundingBox.inflate(radius * burstBoostDecay)
            ) { entity: Entity -> entity !== player }

            for (entity in nearbyEntities) {
                val direction = Vec3(
                    entity.x - player.x,
                    entity.y - player.y,
                    entity.z - player.z
                ).normalize()

                val pushVelocity = direction.scale((pushStrength * burstBoostDecay).toDouble())
                entity.addDeltaMovement(pushVelocity)
                entity.hurtMarked = true
            }
        }

        burstBoostCount = 0
        burstTimer = 0
    }

    private fun renderRadius(level: ServerLevel, center: Vec3, radius: Double) {
        val hitboxParticle: ParticleOptions = ParticleTypes.ELECTRIC_SPARK

        val pointsPerRing = 32
        val yOffset = 0.05

        for (i in 0 until pointsPerRing) {
            val angle = 2 * Math.PI * i / pointsPerRing
            val x = center.x + radius * cos(angle)
            val z = center.z + radius * sin(angle)

            level.sendParticles(
                hitboxParticle,
                x, center.y + yOffset, z,
                1, 0.0, 0.0, 0.0, 0.0
            )
        }
    }

    override fun tick() {
        super.tick()

        if (burstBoostCount > 0) {
            if (burstTimer < 60) {
                ++burstTimer
            } else {
                burstTimer = 0
                burstBoostCount = 0
                val level = target!!.level()
                if (player.level() is ServerLevel) {
                    player.attributes
                        .getInstance(Attributes.ATTACK_SPEED)!!
                        .removeModifier(ATTACK_SPEED_MODIFIER_UUID)
                    level.playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.CREEPER_DEATH,
                        SoundSource.PLAYERS,
                        4f,
                        0.2f
                    )
                }
            }
        }
    }

    companion object {
        val ATTACK_SPEED_MODIFIER_UUID: UUID = UUID.fromString("3e176df4-23ac-4811-8dea-d461bb401352")
    }
}