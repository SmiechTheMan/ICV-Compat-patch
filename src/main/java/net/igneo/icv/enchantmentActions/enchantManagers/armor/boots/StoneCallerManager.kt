package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.Input.Companion.getInput
import net.igneo.icv.enchantmentActions.Input.Companion.getRotation
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.init.ICVUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class StoneCallerManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 300, -10, false, player) {
    var position: Vec3 = Vec3.ZERO

    override fun activate() {

        val level = player.level()
        if (player.level() is ServerLevel) {
            val hitResult = player.pick(20.0, 0f, false)

            if (hitResult.type == HitResult.Type.BLOCK) {
                val blockHitResult = hitResult as BlockHitResult
                position = blockHitResult.location
            } else if (hitResult.type == HitResult.Type.ENTITY) {
                val entityHitResult = hitResult as EntityHitResult
                position = entityHitResult.entity.position()
            }

            for (i in 8 downTo 1) {
                val rot = getRotation(getInput(i)).toFloat()
                val entity = ModEntities.STONE_PILLAR.get().create(level)
                entity!!.setPos(position.add(ICVUtils.getFlatDirection(rot, 3f, 0.0)))
                level.addFreshEntity(entity)
            }
        }
        active = true
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun tick() {
        super.tick()
        if (active) {
            if (activeTicks >= 1200) {
                activeTicks = 0
                active = false
            } else {
                ++activeTicks
            }
        }
    }

    override fun canUse(): Boolean {
        return !active
    }
}


