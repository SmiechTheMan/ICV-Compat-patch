package net.igneo.icv.entity.weapon.comet

import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.sound.COMET_HIT
import net.igneo.icv.sound.PARRY
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.ForgeEventFactory
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState

class CometEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    var hurt: Double = 0.0
    private var hurtTicks: Int = 0
    private var hurtPos: Vec3? = null
    private var peaked: Boolean = false

    init {
        this.addDeltaMovement(Vec3(0.0, 0.23, 0.0))
    }

    override fun <E : GeoEntity?> animController(event: AnimationState<E>?): PlayState? {
        return if (hurt > 0 && hurtTicks < 5) {
            PlayState.STOP
        } else {
            event!!.setAndContinue(IDLE_ANIM)
        }
        return super.animController(event)
    }

    override fun hasFriction(): Boolean {
        return false
    }

    override val gravity: Double
        get() {
            var gravity = -0.0000
            if (!peaked) {
                gravity = -0.02
                if (deltaMovement.y < 0) {
                    peaked = true
                }
            }
            return if (hurt == 0.0) gravity else -0.02
        }

    override fun hurt(pSource: DamageSource, pAmount: Float): Boolean {
        if (level() is ServerLevel) {
        }
        hurtTicks = 0
        hurt += pAmount.toDouble()
        hurtPos = this.position()
        val level = level() as ServerLevel
        if (level() is ServerLevel) {
            level.playSound(null, this.blockPosition(), PARRY.get(), SoundSource.PLAYERS)
        }
        if (pSource.entity != null) {
            this.deltaMovement = pSource.entity!!.lookAngle.normalize().scale(1.0)
        } else if (pSource.sourcePosition != null) {
            val pushVec: Vec3 = pSource.sourcePosition!!.subtract(this.position()).normalize().reverse().scale(1.0)
            this.deltaMovement = Vec3(
                pushVec.x,
                pushVec.y,
                pushVec.z
            )
        }
        return true
    }

    override fun isPickable(): Boolean {
        return true
    }

    override fun onHit(pResult: HitResult) {
        super.onHit(pResult)
        for (entity in level().getEntities(null, this.boundingBox.inflate(4.0))) {
            if (entity !== this) {
                entity.hurt(damageSources().explosion(this, null), (hurt * 1.2f).toFloat())
            }
        }
        this.discard()
    }

    override fun tick() {
        ProjectileUtil.rotateTowardsMovement(this, 1f)
        super.tick()
        val hitresult: HitResult = ProjectileUtil.getHitResultOnMoveVector(
            this
        ) { pTarget: Entity? -> this.canHitEntity(pTarget) }
        if (hitresult.type != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(
                this,
                hitresult
            ) || verticalCollision
        ) {
            this.onHit(hitresult)
        }
        if (hurt > 0 && hurtTicks < 5) {
            this.setPos(hurtPos)
            if (hurtTicks == 4 && level() is ServerLevel) {
                level().playSound(null, this.blockPosition(), COMET_HIT.get(), SoundSource.PLAYERS)
            }
            ++hurtTicks
        }
    }

    companion object {
        protected val IDLE_ANIM: RawAnimation = RawAnimation.begin().thenLoop("idle")
        protected val SHOOT_ANIM: RawAnimation = RawAnimation.begin().thenLoop("shoot")
    }
}
