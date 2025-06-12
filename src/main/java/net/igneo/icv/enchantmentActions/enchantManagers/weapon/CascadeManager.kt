package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.*

class CascadeManager(player: Player?) :
    WeaponEnchantManager(
        EnchantType.WEAPON,
        player!!,
        ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")
    ) {
    private var airTimeStart: Long = 0

    override fun stableCheck(): Boolean {
        return true
    }

    override fun onAttack(entity: Entity?) {
        super.onAttack(entity)
        val fallBoost = max(0.75, log10(player.fallDistance.toDouble())).toFloat()

        if (player.fallDistance > 0.0f && !player.onGround()) {
            player.setDeltaMovement(player.deltaMovement.x, fallBoost.toDouble(), player.deltaMovement.z)
        }

        println(player.fallDistance * 1.2)

        if (player.fallDistance * 1.2 >= 10) {
            player.level().playSound(
                null, player.x, player.y, player.z,
                SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1.5f, 0.5f
            )

        }
    }

    override fun activate() {
        super.activate()
        if (player.onGround()) {
            return
        }

        airTimeStart = System.currentTimeMillis()
        val airTime = System.currentTimeMillis() - airTimeStart
        val accelerationFactor = min((1.0f + (airTime / 1000.0f)).toDouble(), 1.0).toFloat()

        val slamVelocity = Vec3(0.0, (-1 * accelerationFactor).toDouble(), 0.0)
        player.addDeltaMovement(slamVelocity)
        player.hurtMarked = true
    }

    override fun tick() {
        super.tick()
        if (!player.onGround() || airTimeStart == 0L) {
            return
        }

        airTimeStart = 0
        if (player.fallDistance <= 3) {
            return
        }

        val damage = min(6.0, (player.fallDistance * 0.5f).toDouble()).toFloat()
        val radius = min(10.0, (player.fallDistance * 1.2f).toDouble()).toFloat()

        renderRadius(player.level() as ServerLevel, player.position(), radius.toDouble())
        val nearbyEntities = player.level().getEntities(player, player.boundingBox.inflate(radius.toDouble()))

        for (entity in nearbyEntities) {
            val push = entity.position().subtract(player.position()).normalize().scale(2.0)
            entity.hurt(entity.damageSources().playerAttack(player), damage)
            entity.addDeltaMovement(push)
            entity.hurtMarked = true
        }
        player.fallDistance = 0f
    }

    private fun renderRadius(level: ServerLevel, center: Vec3, radius: Double) {
        val hitboxParticle: ParticleOptions = ParticleTypes.ELECTRIC_SPARK

        val pointsPerRing = 32 * player.fallDistance.toInt()

        for (i in 0 until pointsPerRing) {
            val angle = 2 * Math.PI * i / pointsPerRing
            val x = center.x + radius * cos(angle)
            val z = center.z + radius * sin(angle)

            level.sendParticles(
                hitboxParticle,
                x, center.y, z,
                1, 0.0, 0.0, 0.0, 0.0
            )
        }
    }
}