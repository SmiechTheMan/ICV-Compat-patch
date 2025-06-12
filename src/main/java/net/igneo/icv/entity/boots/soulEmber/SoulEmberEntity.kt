package net.igneo.icv.entity.boots.soulEmber

import net.igneo.icv.Utils.getFlatDirection
import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

class SoulEmberEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    override fun getStepHeight(): Float {
        return 1.5f
    }

    override fun tick() {
        super.tick()
        if (this.owner != null) {
            val baseVec =
                this.owner!!.eyePosition.subtract(this.position()).normalize()
            this.addDeltaMovement(Vec3(baseVec.x, 0.0, baseVec.z).scale(0.22))
        } else {
            discard()
        }
    }

    override fun canAddPassenger(pPassenger: Entity): Boolean {
        return pPassenger === this.owner
    }

    override val airFriction: Double
        get() = 0.2

    override fun isNoGravity(): Boolean {
        return true
    }

    override fun onHitEntity(pResult: EntityHitResult) {
        super.onHitEntity(pResult)
        val entity = pResult.entity
        if (pResult.entity == this.owner) {
            this.discard()
        } else if (pResult.entity is LivingEntity) {
            entity.hurt(damageSources().inFire(), 5f)
        }
    }

    override fun hurt(pSource: DamageSource, pAmount: Float): Boolean {
        if (pSource.entity != null) {
            this.deltaMovement = getFlatDirection(pSource.entity!!.yRot, 3f, 0.0)
        }
        return super.hurt(pSource, pAmount)
    }

    override fun isPickable(): Boolean {
        return true
    }
}
