package net.igneo.icv.entity.chestplate.abyssStone

import net.igneo.icv.entity.ICVEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class AbyssStoneEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifetime = 0

    override fun tick() {
        super.tick()
        for (entity in level().getEntities(null, this.boundingBox.inflate(5.0))) {
            if (entity === this.owner || entity is ItemEntity) {
                return
            }

            val pushDirection = entity.position().subtract(this.position()).normalize().scale(0.5)
            entity.deltaMovement = entity.deltaMovement.add(pushDirection)

            entity.hurt(level().damageSources().fellOutOfWorld(), 3f)
        }

        if (lifetime < 200) {
            lifetime++
        } else {
            this.discard()
        }
    }

    override fun canBeCollidedWith(): Boolean {
        return false
    }
}
