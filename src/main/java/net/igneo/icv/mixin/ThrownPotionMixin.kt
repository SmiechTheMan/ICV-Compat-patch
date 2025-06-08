package net.igneo.icv.mixin

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique

@Mixin(value = [ThrownPotion::class])
class ThrownPotionMixin(pEntityType: EntityType<out ThrowableItemProjectile?>, pLevel: Level) :
    ThrowableItemProjectile(pEntityType, pLevel) {
    @Unique
    private var increaseSpeed = false

    override fun tick() {
        if (!increaseSpeed) {
            println("increasing speed!")
            this.deltaMovement = deltaMovement.scale(1.5)
            //this.addDeltaMovement(this.getDeltaMovement());
            increaseSpeed = true
        }
        super.tick()
    }

    override fun getDefaultItem(): Item? {
        return null
    }
}
