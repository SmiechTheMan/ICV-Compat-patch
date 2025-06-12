package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class GravityWellManager(player: Player?) : ArmorEnchantManager(EnchantType.HELMET, 300, 10, false, player) {
    private var surfaceDirection: Direction? = null
    private var position: Vec3? = null

    override fun onOffCoolDown(player: Player?) = Unit
    override val indicator: EnchantIndicator = StasisCooldownIndicator(this)

    override fun activate() {
        if (player.level() !is ServerLevel) return
        (player.pick(5.0, 0.0f, false) as? BlockHitResult)?.let {
            surfaceDirection = it.direction
            position = it.location
            applyGravityEffect(it.direction)
        }
    }

    private fun applyGravityEffect(direction: Direction) {
        val level = player.level() as? ServerLevel ?: return
        val pos = position ?: return
        val entities = ICVUtils.collectEntitiesBox(level, pos, 7.5)
        val pushVec = when (direction) {
            Direction.UP -> Vec3(0.0, PUSH_STRENGTH * 0.2, 0.0)
            Direction.DOWN -> Vec3(0.0, -PUSH_STRENGTH, 0.0)
            Direction.NORTH -> Vec3(0.0, 0.2, -PUSH_STRENGTH * 2)
            Direction.SOUTH -> Vec3(0.0, 0.2, PUSH_STRENGTH * 2)
            Direction.EAST -> Vec3(PUSH_STRENGTH * 2, 0.2, 0.0)
            Direction.WEST -> Vec3(-PUSH_STRENGTH * 2, 0.2, 0.0)
            else -> Vec3.ZERO
        }

        for (entity in entities) {
            val distance = entity.position().distanceTo(pos)
            val strength = PUSH_STRENGTH * (1 - distance / EFFECT_RADIUS)
            if (strength <= 0) continue

            entity.deltaMovement = entity.deltaMovement.add(pushVec.scale(strength))
            if (entity is LivingEntity && direction == Direction.UP) entity.fallDistance = 0f
        }
    }

    companion object {
        private const val PUSH_STRENGTH = 1.5
        private const val EFFECT_RADIUS = 5.0
    }
}
