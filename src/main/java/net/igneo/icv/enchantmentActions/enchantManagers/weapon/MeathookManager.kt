package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class MeathookManager(player: Player?) :
    WeaponEnchantManager(
        EnchantType.WEAPON,
        player!!,
        ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")
    ) {
    private val meat = HashMap<Entity, Int>()

    override fun onAttack(entity: Entity?) {
        super.onAttack(entity)
        meat[entity!!] = 200
    }

    override fun tick() {
        super.tick()
        for (entity in meat.keys) {
            meat.replace(entity, meat[entity]!! - 1)
            if (meat[entity]!! <= 0) {
                meat.remove(entity)
            }
        }
    }

    override fun activate() {
        super.activate()
        var x = 0.0
        var y = 0.0
        var z = 0.0
        for (entity in meat.keys) {
            x += entity.x
            y += entity.y
            z += entity.z
        }
        x /= meat.keys.size
        y /= meat.keys.size
        z /= meat.keys.size
        val average = Vec3(x, y, z)

        for (entity in meat.keys) {
            if (entity.distanceToSqr(average) < 70) {
                val pushVec = entity.position().subtract(average).normalize().reverse()
                entity.addDeltaMovement((if (entity.onGround()) Vec3(pushVec.x, 0.5, pushVec.z) else pushVec))
            }
        }

        meat.clear()
    }
}
