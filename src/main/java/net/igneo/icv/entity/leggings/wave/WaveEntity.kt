package net.igneo.icv.entity.leggings.wave

import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.boots.surfWave.SurfWaveEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class WaveEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0
    private var storedMotion: Vec3 = Vec3.ZERO

    fun setTrajectory(vec: Vec3) {
        storedMotion = vec
    }

    override fun getStepHeight(): Float {
        return 1.5f
    }

    override fun tick() {
        super.tick()
        for (entity in level().getEntities(null, this.boundingBox.inflate(3.0))) {
            if (entity !is WaveEntity && entity !is SurfWaveEntity) {
                entity.addDeltaMovement(storedMotion)
            }
        }
        deltaMovement = storedMotion
        if (lifetime < 20) {
            ++lifetime
        } else {
            this.discard()
        }
    }

    override fun canAddPassenger(pPassenger: Entity): Boolean {
        return pPassenger === this.owner
    }
}
