package net.igneo.icv.entity.helmet.blackHole

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState

class BlackHoleEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {

    override fun <E : GeoEntity?> animController(event: AnimationState<E>?): PlayState? {
        return event!!.setAndContinue(IDLE_ANIM)
    }

    override fun isNoGravity(): Boolean {
        return true
    }

    override fun tick() {
        super.tick()
        if (this.tickCount > 400 || this.owner == null) {
            this.discard()
        }

        pull()
    }

    private fun pull() {
        for (entity in level().getEntities(null, this.boundingBox.inflate(6.0))) {
            if (entity !== this) {
                val scale = 0.05
                var pushVec = entity.position().subtract(this.position()).reverse()
                pushVec = pushVec.normalize().scale(scale)
                entity.addDeltaMovement(pushVec)
                if (entity.distanceTo(this) < 2) {
                    entity.addDeltaMovement(pushVec)
                    entity.hurt(damageSources().cramming(), 3f)
                }
            }
        }
    }

    override val airFriction: Double
        get() = 0.1


    override fun isPickable(): Boolean {
        return true
    }

    override fun hurt(pSource: DamageSource, pAmount: Float): Boolean {
        if (pSource.entity == null) {
            val pushVec: Vec3 = pSource.sourcePosition!!.subtract(this.position()).normalize().reverse().scale(1.0)
            this.addDeltaMovement(
                Vec3(
                    pushVec.x,
                    0.0,
                    pushVec.z
                )
            )
        } else if (pSource.sourcePosition != null) {
            this.addDeltaMovement(pSource.entity!!.lookAngle.normalize().scale(0.2))
        }
        return super.hurt(pSource, pAmount)
    }

    override fun onHitBlock(pResult: BlockHitResult) {
    }

    companion object {
        protected val IDLE_ANIM: RawAnimation = RawAnimation.begin().thenLoop("idle")
    }
}
