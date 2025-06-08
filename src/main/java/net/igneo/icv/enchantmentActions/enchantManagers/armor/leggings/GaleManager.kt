package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class GaleManager(player: Player?) :
    ArmorEnchantManager(EnchantType.LEGGINGS, 300, -10, false, player) {
    override fun activate() {
        println("activating")
        for (entity in player!!.level().getEntities(null, player!!.boundingBox.inflate(5.0))) {
            var scale = 2f
            if (entity is LivingEntity) {
                scale = 4f
            }
            entity.deltaMovement = ICVUtils.getFlatDirection(player!!.yRot, scale, 0.5)
        }
        resetCoolDown()
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)
}


