package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate

import net.igneo.icv.Utils.collectEntitiesBox
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.collections.HashMap

class ImmolateManager(player: Player?) :
    ArmorEnchantManager(EnchantType.CHESTPLATE, 300, -10, false, player) {
    private val markedEntities = HashMap<UUID, Entity>()

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun activate() {
        val level = player.level()
        if (player.level() !is ServerLevel) {
            return
        }
        level.playSound(
            null, player.x, player.y, player.z,
            SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0f, 0.8f
        )

        val entities = collectEntitiesBox(player.level(), player.position(), IMMOLATE_RADIUS)

        for (entity in entities) {
            if (entity === player) {
                continue
            }

            entity.setSecondsOnFire(10)
            entity.addTag(IMMOLATE_TAG)

            markedEntities[entity.uuid] = entity
        }
    }

    override fun onOffCoolDown(player: Player?) {
        TODO("Not yet implemented")
    }

    override fun tick() {
        super.tick()

        val level = player.level() as ServerLevel
        if (player.level() !is ServerLevel) {
            return
        }

        val iterator = markedEntities.keys.iterator()

        while (iterator.hasNext()) {
            val id = iterator.next()
            val entity = markedEntities[id]

            if (entity == null || !entity.isAddedToWorld) {
                if (entity is ItemEntity) {
                    createKnockbackEffect(level, entity.position())
                }
                iterator.remove()
                continue
            }

            if (!entity.isAlive) {
                if (entity.tags.contains(IMMOLATE_TAG)) {
                    createKnockbackEffect(level, entity.position())
                }
                iterator.remove()
                continue
            }

            if (!entity.isOnFire) {
                entity.removeTag(IMMOLATE_TAG)
                iterator.remove()
                continue
            }

            if (level.gameTime % 10 == 0L) {
                level.sendParticles(
                    ParticleTypes.FLAME,
                    entity.x, entity.y + 0.5f, entity.z,
                    3, 0.2, 0.2, 0.2, 0.01
                )
            }
        }
    }

    private fun createKnockbackEffect(level: ServerLevel, position: Vec3) {
        level.playSound(
            null, position.x, position.y, position.z,
            SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f
        )

        level.sendParticles(
            ParticleTypes.EXPLOSION_EMITTER,
            position.x, position.y, position.z,
            1, 0.0, 0.0, 0.0, 0.0
        )

        level.sendParticles(
            ParticleTypes.LARGE_SMOKE,
            position.x, position.y, position.z,
            15, 0.5, 0.5, 0.5, 0.1
        )

        val nearbyEntities = collectEntitiesBox(player!!.level(), position, IMMOLATE_RADIUS)

        for (nearbyEntity in nearbyEntities) {
            nearbyEntity.hurt(level.damageSources().explosion(player, null), 1f)

            val knockbackDir = nearbyEntity.position().subtract(position).normalize()

            var knockbackStrength = 1.5

            if (!player!!.onGround()) {
                knockbackStrength = 0.8
            }

            nearbyEntity.deltaMovement = nearbyEntity.deltaMovement.add(
                knockbackDir.x * knockbackStrength,
                0.7,
                knockbackDir.z * knockbackStrength
            )

            nearbyEntity.setSecondsOnFire(10)
            nearbyEntity.addTag(IMMOLATE_TAG)

            nearbyEntity.hasImpulse = true
        }
    }

    override fun canUse(): Boolean {
        return stableCheck()
    }

    companion object {
        private const val IMMOLATE_RADIUS = 5.0
        private const val IMMOLATE_TAG = "Immolate_Fire"
    }
}