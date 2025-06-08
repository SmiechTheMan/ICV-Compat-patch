package net.igneo.icv.entity

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.util.GeckoLibUtil
import kotlin.math.atan2

abstract class ICVEntity protected constructor(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    Projectile(pEntityType, pLevel), GeoEntity {
    private val geoCache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)


    override fun registerControllers(controllers: ControllerRegistrar) {
        controllers.add(AnimationController(
            this, "icv", 6
        ) { event: AnimationState<ICVEntity>? ->
            this.animController(
                event
            )
        })
    }

    protected open fun <E : GeoEntity?> animController(event: AnimationState<E>?): PlayState? {
        return PlayState.STOP
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return geoCache
    }

    override fun defineSynchedData() {
    }

    open val groundFriction: Double
        get() = 0.8

    open val airFriction: Double
        get() = 0.001

    val waterFriction: Double
        get() = 0.01

    open val gravity: Double
        get() = -0.05

    open fun hasFriction(): Boolean {
        return true
    }

    override fun tick() {
        super.tick()
        runPhysics()
        runHit()
    }

    private fun runHit() {
        val hitresult: HitResult = ProjectileUtil.getHitResultOnMoveVector(
            this
        ) { pTarget: Entity? -> this.canHitEntity(pTarget) }
        if (hitresult.type != HitResult.Type.MISS) {
            this.onHit(hitresult)
        }
    }

    fun runPhysics() {
        if (!this.onGround() && !this.isNoGravity) {
            this.addDeltaMovement(Vec3(0.0, gravity, 0.0))
        }
        if (hasFriction()) {
            var currentVelocity: Vec3 = this.deltaMovement
            var friction = if (this.onGround()) groundFriction else airFriction

            friction = if (level().getBlockState(this.blockPosition()).isAir) friction else waterFriction
            currentVelocity = Vec3(currentVelocity.scale(friction).x, 0.0, currentVelocity.scale(friction).z)
            currentVelocity = deltaMovement.subtract(currentVelocity)

            if (currentVelocity.lengthSqr() < 0.0001) {
                this.deltaMovement = Vec3.ZERO
            } else {
                this.deltaMovement = currentVelocity
            }
        }

        this.move(MoverType.SELF, deltaMovement.scale(1.5))
    }

    fun faceDirection(dir: Vec3) {
        val dx: Double = dir.x
        val dy: Double = dir.y
        val dz: Double = dir.z

        val yaw = (Math.toDegrees(atan2(-dx, dz))).toFloat()

        this.yRot = yaw
    }
}
