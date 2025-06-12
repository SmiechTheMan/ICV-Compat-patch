package net.igneo.icv.entity.weapon.snakeBite

import net.igneo.icv.enchantmentActions.enchantManagers.weapon.ViperManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.init.ICVUtils.GetManagerForType.getManagerForType
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class SnakeBiteEntity(pEntityType: EntityType<out Projectile?>?, pLevel: Level?) :
    ICVEntity(pEntityType!!, pLevel!!) {
    var lifeTime: Int = 0

    override val gravity: Double
        get() = 0.0

    override fun tick() {
        super.tick()
        if (lifeTime > 5) {
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
        if (pResult.entity is LivingEntity) {
            val potionBonus = (if (entity.activeEffects.isEmpty()) 0 else 4).toDouble()
            entity.addEffect(MobEffectInstance(MobEffects.POISON, 100, 2))
            entity.hurt(damageSources().magic(), (4 + potionBonus).toFloat())
        }
    }

    override fun remove(pReason: RemovalReason) {
        val manager = (getManagerForType(
            (this.owner as Player?)!!,
            ViperManager::class.java
        ) as ViperManager?)
        if (manager != null) manager.child = null
        super.remove(pReason)
    }
}
