package net.igneo.icv.entity.boots.stonePillar

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class StonePillarEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0
    private var health: Float = 40f

    override fun getStepHeight(): Float {
        return 1.5f
    }

    override fun tick() {
        super.tick()
        if (this.health <= 0) {
            this.discard()
        }
        if (lifetime < 400) {
            ++lifetime
        } else {
            this.discard()
        }
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun hurt(pSource: DamageSource, pAmount: Float): Boolean {
        health -= pAmount
        return super.hurt(pSource, pAmount)
    }
}
