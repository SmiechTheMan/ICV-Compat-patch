package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.Input.Companion.flattenInput
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

class PlanarShiftManager(player: Player?) :
    ArmorEnchantManager(EnchantType.CHESTPLATE, 300, -10, true, player) {
    var position: Vec3? = null
    private val blackList: List<Entity> = ArrayList()

    override fun activate() {
        println("activating")
        position = player.position()
        active = true
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun canUse(): Boolean {
        return !active
    }

    override fun dualActivate() {
        resetCoolDown()
        val dir = player.direction
        println(dir)

        val dist = 1f
        val shiftRot = when (dir) {
            Direction.DOWN -> null
            Direction.UP -> null
            Direction.NORTH -> Vec3(0.0, 0.0, -dist.toDouble())
            Direction.SOUTH -> Vec3(0.0, 0.0, dist.toDouble())
            Direction.WEST -> Vec3(-dist.toDouble(), 0.0, 0.0)
            Direction.EAST -> Vec3(dist.toDouble(), 0.0, 0.0)
        }
        val yaw = atan2(shiftRot!!.z, shiftRot.x)
        val rot = Math.toDegrees(yaw).toFloat() - 90
        println(shiftRot)
        println(rot)
        println(
            ICVUtils.getFlatInputDirection(
                rot, flattenInput(
                    enchVar!!.input
                ), 10f, 0.0
            )
        )
        val scale = Vec3(5.0, 5.0, 5.0)
        for (entity in player.level().getEntities(null, AABB(position!!.subtract(scale), position!!.add(scale)))) {
            entity.setPos(
                entity.position().add(
                    ICVUtils.getFlatInputDirection(
                        rot, flattenInput(
                            enchVar!!.input
                        ), 10f, 0.0
                    )
                )
            )
        }
        active = false
        resetCoolDown()
    }

    override var isDualUse: Boolean
        get() = true
        set(isDualUse) {
            super.isDualUse = isDualUse
        }
}


