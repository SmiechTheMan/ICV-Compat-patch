package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings

import net.igneo.icv.client.indicators.BlackHoleIndicator
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.world.entity.player.Player

class TempestManager(player: Player?) :
    ArmorEnchantManager(EnchantType.LEGGINGS, 300, -10, false, player) {
    override fun activate() {
        if (player.level().isClientSide) {
            player.deltaMovement = ICVUtils.getFlatInputDirection(player.yRot, enchVar!!.input, 1.5f, 0.5)
        }
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = BlackHoleIndicator(this)

    override fun resetCoolDown() {
        addCoolDown((maxCoolDown / 3))
    }

    override val isOffCoolDown: Boolean
        get() = getCoolDown() <= maxCoolDown - (maxCoolDown / 3)

    override fun canUse(): Boolean {
        return true
    }
}
