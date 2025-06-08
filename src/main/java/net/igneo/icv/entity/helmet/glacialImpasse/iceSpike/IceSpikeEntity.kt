package net.igneo.icv.entity.helmet.glacialImpasse.iceSpike

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class IceSpikeEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0

    override fun getStepHeight(): Float {
        return 1.5f
    }

    override fun tick() {
        super.tick()

        if (lifetime < 400) {
            lifetime++
        } else {
            this.discard()
        }

        hurtPlayer()
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    private fun hurtPlayer() {
        for (entity in level().getEntities(null, this.boundingBox.inflate(1.01))) {
            if (entity === this.owner) {
                return
            }
            entity.hurt(damageSources().freeze(), 3f)
        }
    }
}
