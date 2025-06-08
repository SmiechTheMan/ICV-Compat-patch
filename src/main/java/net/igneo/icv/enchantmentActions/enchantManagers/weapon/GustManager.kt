package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.MovePlayerS2CPacket
import net.igneo.icv.sound.ModSounds
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class GustManager(player: Player?) :
    WeaponEnchantManager(EnchantType.WEAPON, player, ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")) {
    override fun activate() {
        super.activate()
        if (System.currentTimeMillis() < gustDelay + GUST_COOLDOWN) {
            return
        }

        val level = player!!.level()
        if (player!!.level() !is ServerLevel) {
            return
        }
        level.playSound(null, player!!.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS, 0.5f, 1.2f)

        val radius = 5.0
        val halfAngleDegrees = 45.0
        val cosThreshold = cos(Math.toRadians(halfAngleDegrees))

        val playerLook = player!!.lookAngle.normalize()

        renderConeHitbox(level as ServerLevel, player!!, playerLook, radius, halfAngleDegrees)

        val nearbyEntities: List<Entity> = level.getEntities(player,
            player!!.boundingBox.inflate(radius)
        ) { entity: Entity -> entity !== player }

        for (entity in nearbyEntities) {
            val toEntity = Vec3(
                entity.x - player!!.x,
                entity.y - player!!.y,
                entity.z - player!!.z
            )
            val distance = toEntity.length()
            if (distance >= radius) {
                continue
            }
            val toEntityNormalized = toEntity.normalize()
            val dot = playerLook.dot(toEntityNormalized)
            if (dot <= cosThreshold) {
                continue
            }
            val launchVelocity = Vec3(entity.deltaMovement.x, 0.8, entity.deltaMovement.z)
            entity.deltaMovement = launchVelocity
            entity.hurtMarked = true

            if (entity is ServerPlayer) {
                ModMessages.sendToPlayer(MovePlayerS2CPacket(Vec3(0.0, 1.0, 0.0)), entity)
            }

            level.sendParticles(
                ParticleTypes.CLOUD, entity.x, entity.y, entity.z,
                5, 0.3, 0.3, 0.3, 0.05
            )
            level.playSound(null, entity.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS, 1.0f, 1.0f)
        }
    }

    private fun renderConeHitbox(
        level: ServerLevel,
        player: Player,
        direction: Vec3,
        radius: Double,
        halfAngleDegrees: Double
    ) {
        val playerPos = player.position().add(0.0, 1.0, 0.0)
        val hitboxParticle: ParticleOptions = ParticleTypes.END_ROD

        val particlesPerRing = 16
        val numRings = 5

        for (ring in 1..numRings) {
            val ringRadius = (radius * ring) / numRings

            val circleRadius = tan(Math.toRadians(halfAngleDegrees)) * ringRadius

            for (i in 0 until particlesPerRing) {
                val angle = 2 * Math.PI * i / particlesPerRing

                val up = Vec3(0.0, 1.0, 0.0)
                var right = direction.cross(up).normalize()
                if (right.length() < 0.001) {
                    right = Vec3(1.0, 0.0, 0.0)
                }
                val newUp = right.cross(direction).normalize()

                val offset = right.scale(cos(angle) * circleRadius)
                    .add(newUp.scale(sin(angle) * circleRadius))

                val particlePos = playerPos.add(direction.scale(ringRadius)).add(offset)

                level.sendParticles(
                    hitboxParticle,
                    particlePos.x, particlePos.y, particlePos.z,
                    1, 0.0, 0.0, 0.0, 0.0
                )
            }
        }
    }

    override val damageBonus: Float
        get() = 0f

    companion object {
        var gustDelay: Long = 0
        private const val GUST_COOLDOWN: Long = 2000
    }
}