package net.igneo.icv.entity.helmet.divineLightningRod

import net.igneo.icv.entity.ICE_SPIKE
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.STONE_PILLAR
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import kotlin.math.max
import kotlin.math.min

class DivineLightningRodEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lightningTimer = 0
    private var lifetime = 0
    private var health = 40f

    private fun isBlockedPathToTarget(target: Entity): Boolean {
        if (target === this.owner) {
            return true
        }

        val rodPos = position().add(0.0, 1.0, 0.0)
        val targetPos = target.position().add(0.0, (target.eyeHeight * 0.5f).toDouble(), 0.0)

        val blockResult = level().clip(
            ClipContext(
                rodPos,
                targetPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
            )
        )

        if (blockResult.type == HitResult.Type.BLOCK) {
            return true
        }


        val checkBox = AABB(
            min(rodPos.x, targetPos.x) - 1.0,
            min(rodPos.y, targetPos.y) - 1.0,
            min(rodPos.z, targetPos.z) - 1.0,
            max(rodPos.x, targetPos.x) + 1.0,
            max(rodPos.y, targetPos.y) + 1.0,
            max(rodPos.z, targetPos.z) + 1.0
        )

        val entitiesInPath = level().getEntities(this, checkBox)

        for (entity in entitiesInPath) {
            if (entity === target || entity === this) {
                continue
            }

            if (BLOCKING_ENTITIES.contains(entity.type)) {
                val entityBox: AABB = entity.boundingBox.inflate(0.5)

                if (entityBox.clip(rodPos, targetPos).isPresent) {
                    return true
                }
            } else if (entity is LivingEntity) {
                val entityBox: AABB = entity.getBoundingBox().inflate(0.5)

                if (entityBox.clip(rodPos, targetPos).isPresent) {
                    return true
                }
            }
        }

        return false
    }

    override fun tick() {
        super.tick()

        if (lifetime < MAX_LIFETIME) {
            lifetime++
        } else {
            this.discard()
            return
        }

        lightningTimer++

        if (lightningTimer % LIGHTNING_INTERVAL == 0) {
            strikeLightning()
        }
    }

    private fun strikeLightning() {
        val nearbyEntities = level().getEntities(
            this,
            this.boundingBox.inflate(LIGHTNING_RADIUS)
        )

        for (entity in nearbyEntities) {
            if (entity === this.owner) {
                continue
            }

            if (isBlockedPathToTarget(entity)) {
                continue
            }

            entity.hurt(damageSources().lightningBolt(), LIGHTNING_DAMAGE.toFloat())
        }
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun hurt(pSource: DamageSource, pAmount: Float): Boolean {
        health -= pAmount

        if (health <= 0) {
            this.discard()
        }

        return super.hurt(pSource, pAmount)
    }

    companion object {
        private const val LIGHTNING_INTERVAL = 200
        private const val MAX_LIFETIME = 600
        private const val LIGHTNING_DAMAGE = 15
        private const val LIGHTNING_RADIUS = 20.0
        private val BLOCKING_ENTITIES: List<EntityType<*>> = listOf<EntityType<*>>(
            ICE_SPIKE.get(),
            STONE_PILLAR.get()
        )
    }
}