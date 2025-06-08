package net.igneo.icv.entity.chestplate.soulSpider

import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.chestplate.soulOrb.SoulOrbEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.abs

class SoulSpiderEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var orb: SoulOrbEntity? = null
    private var orbObtained: Boolean = false

    override fun tick() {
        super.tick()
        if (orb == null) {
            if (orbObtained) this.discard()
        } else if (inRange()) {
            val pushVec = Vec3(orb!!.position().x - position().x, 0.0, orb!!.position().z - position().z)
            this.addDeltaMovement(pushVec.normalize().scale(0.2))
        }
    }

    private fun inRange(): Boolean {
        if (orb == null) {
            if (orbObtained) {
                this.discard()
                return true
            } else {
                return true
            }
        }
        return (abs(position().x - orb!!.position().x) + abs(position().z - orb!!.position().z)) > 4
    }

    override val groundFriction: Double
        get() = if (inRange()) 0.0 else 0.8

    fun setOrb(entity: SoulOrbEntity?) {
        orb = entity
        orbObtained = true
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

    override fun onHitEntity(pResult: EntityHitResult) {
        val entity = pResult.entity as LivingEntity
        if (pResult.entity is LivingEntity) {
            entity.hurt(damageSources().magic(), 8f)
        }
    }

    override fun getStepHeight(): Float {
        return 1.5f
    }
}
