package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings

import net.igneo.icv.Utils.getFlatDirection
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.Input.Companion.getInput
import net.igneo.icv.enchantmentActions.Input.Companion.getRotation
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.WAVE
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

class TsunamiManager(player: Player?) :
    ArmorEnchantManager(EnchantType.LEGGINGS, 300, -10, false, player) {
    override fun activate() {
        val level = player.level()
        if (player.level() is ServerLevel) {
            for (i in 8 downTo 1) {
                val rot = getRotation(getInput(i)).toFloat()
                val entity = WAVE.get().create(level)
                entity!!.setPos(player.position().add(getFlatDirection(rot, 2f, 0.0)))
                entity.setTrajectory(getFlatDirection(rot, 1f, 0.0))
                level.addFreshEntity(entity)
            }
        }
        resetCoolDown()
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)
}


