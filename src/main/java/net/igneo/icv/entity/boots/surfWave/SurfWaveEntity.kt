package net.igneo.icv.entity.boots.surfWave

import net.igneo.icv.enchantmentActions.Input
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.leggings.wave.WaveEntity
import net.igneo.icv.init.ICVUtils.getFlatDirection
import net.igneo.icv.init.ICVUtils.getFlatInputDirection
import net.igneo.icv.init.LodestoneParticles.waveParticles
import net.igneo.icv.init.LodestoneParticles.waveParticlesBright
import net.igneo.icv.init.ParticleShapes.renderLineList
import net.igneo.icv.sound.ModSounds
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.util.NonNullConsumer
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState
import kotlin.math.atan2

class SurfWaveEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0
    private var storedMotion: Vec3 = Vec3.ZERO
    private val entities = ArrayList<Entity>()

    override fun getStepHeight(): Float {
        return 1.5f
    }

    override fun <E : GeoEntity?> animController(event: AnimationState<E>?): PlayState? {
        return event!!.setAndContinue(IDLE_ANIM)
    }

    override fun tick() {
        super.tick()
        if (passengers.isNotEmpty()) {
            storedMotion = getFlatDirection(this.firstPassenger!!.yRot, 0.4f, 0.0)
            this.firstPassenger!!.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent(
                    NonNullConsumer { enchVar: PlayerEnchantmentActions ->
                        storedMotion = storedMotion.add(
                            getFlatInputDirection(
                                this.firstPassenger!!.yRot, enchVar.input, 0.3f, 0.0
                            )
                        )
                        if (enchVar.input == Input.FORWARD) {
                            storedMotion = storedMotion.scale(1.3)
                        }
                    })
        }
        for (entity in level().getEntities(null, this.boundingBox.inflate(3.5))) {
            if (entity !== this.owner && entity !is WaveEntity && entity !is SurfWaveEntity) {
                waveParticlesBright(this.level(), entity.position(), Vec3.ZERO)
                entity.addDeltaMovement(storedMotion.scale(1.2))
            }
            if (!entities.contains(entity)) {
                level().playSound(
                    null, position().x, position().y,
                    position().z,
                    ModSounds.SURF_PICKUP.get(),
                    SoundSource.PLAYERS, 0.5f, 1.0f
                )
                entities.add(entity)
            }
        }
        deltaMovement = storedMotion
        if (lifetime < 200) {
            ++lifetime
        } else {
            this.discard()
        }
        faceDirection(this.deltaMovement)


        val scale = 3f
        val mscale = 3f
        val yaw = (Math.toDegrees(atan2(-this.lookAngle.x, this.lookAngle.z))).toFloat()
        val start: Vec3 = getFlatDirection(yaw + 130, scale, 0.0)
            .add(this.position())
            .add(this.lookAngle.scale(mscale.toDouble()))
        val stop: Vec3 = getFlatDirection(yaw - 130, scale, 0.0)
            .add(this.position())
            .add(this.lookAngle.scale(mscale.toDouble()))

        for (pos in renderLineList(
            level(),
            position().add(this.lookAngle.scale(mscale.toDouble())), stop, 2
        )) {
            val realPos = pos.add(Math.random(), 0.0, Math.random())
            waveParticles(level(), realPos, this.lookAngle.scale(0.3))
        }
        for (pos in renderLineList(
            level(),
            position().add(this.lookAngle.scale(mscale.toDouble())), start, 2
        )) {
            val realPos = pos.add(Math.random(), 0.0, Math.random())
            waveParticles(level(), realPos, this.lookAngle.scale(0.3))
        }
    }

    override fun canAddPassenger(pPassenger: Entity): Boolean {
        return pPassenger === this.owner
    }

    companion object {
        protected val IDLE_ANIM: RawAnimation = RawAnimation.begin().thenLoop("idle")
    }
}
