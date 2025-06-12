package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

class FinesseManager(player: Player?) :
    WeaponEnchantManager(
        EnchantType.WEAPON,
        player!!,
        ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")
    ) {
    override fun onAttack(entity: Entity?) {
        var redirect = player.deltaMovement.reverse()
        var scale = 6.0
        if (!player.onGround()) {
            scale = 3.0
        }
        val Yscale = 1.2
        var d0 = redirect.x * scale
        var d1 = redirect.y * Yscale
        var d2 = redirect.z * scale

        if (d0 > 2) d0 = 2.0
        if (d0 < -2) d0 = -2.0

        if (d1 > 1) d1 = 1.0
        if (d1 < -1) d1 = -1.0

        if (d2 > 2) d2 = 2.0
        if (d2 < -2) d2 = -2.0

        redirect = Vec3(d0, d1, d2)
        player.deltaMovement = redirect
    }

    override fun activate() {
        super.activate()
        val yaw = Math.toRadians(player.yRot.toDouble())
        val x = -sin(yaw)
        val y = 0.25
        val z = cos(yaw)
        val scale = 2.0

        val flatDirection = Vec3(x * scale, y, z * scale)
        player.deltaMovement = flatDirection
    }

    override val damageBonus: Float
        get() = 20f
}
