package net.igneo.icv.entity.weapon.fireRing

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class FireRingEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    var lifeTime: Int = 0

    override val gravity: Double
        get() = 0.0

    override fun tick() {
        super.tick()
        if (lifeTime > 10) {
            this.discard()
        } else {
            ++lifeTime
        }
    }

    override fun isPushable(): Boolean {
        return true
    }

    override fun isPickable(): Boolean {
        return true
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun onHitBlock(pResult: BlockHitResult) {
        this.discard()
    }

    override fun onHit(pResult: HitResult) {
        super.onHit(pResult)
        println("beuh")
    }

    override fun onHitEntity(pResult: EntityHitResult) {
        val entity = pResult.entity as LivingEntity
        if (pResult.entity is LivingEntity && entity !== this.owner) {
            entity.setSecondsOnFire(3)
            entity.hurt(damageSources().onFire(), 4f)
        }
    }
}
