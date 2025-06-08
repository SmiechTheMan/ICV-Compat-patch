package net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class IceSpikeSpawnerEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0

    override fun isNoGravity(): Boolean {
        return true
    }

    override fun tick() {
        super.tick()
        if (this.owner == null) {
            return
        }
        this.deltaMovement = this.owner!!.getViewVector(1.0f).scale(0.3)

        if (lifetime < 200) {
            lifetime++
        } else {
            this.discard()
        }
    }
}
