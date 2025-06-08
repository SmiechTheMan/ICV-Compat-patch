package net.igneo.icv.entity.leggings.voidSpike

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult

class VoidSpikeEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0
    var launched: Boolean = false

    override fun tick() {
        super.tick()
        if (lifetime < 400) {
            ++lifetime
        } else {
            println("lifetime")
            this.discard()
        }
        if (this.onGround() && launched) {
            this.discard()
        }
    }

    override fun onHitEntity(pResult: EntityHitResult) {
        val entity = pResult.entity as LivingEntity
        if (pResult.entity != this.owner && pResult.entity is LivingEntity && !level().isClientSide) {
            entity.hurt(damageSources().fellOutOfWorld(), 5f)
            println("on hit")
            discard()
        }
        super.onHitEntity(pResult)
    }
}
