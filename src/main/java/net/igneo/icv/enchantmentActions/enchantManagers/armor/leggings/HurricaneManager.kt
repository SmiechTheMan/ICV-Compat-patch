package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings

import net.igneo.icv.Utils.getFlatDirection
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class HurricaneManager(player: Player?) :
    ArmorEnchantManager(EnchantType.LEGGINGS, 300, -10, false, player) {
    override fun activate() {
        println("activating")
        player.deltaMovement = getFlatDirection(player.yRot, 2f, 0.5)
        active = true
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun canUse(): Boolean {
        return !active
    }

    override fun tick() {
        super.tick()
        if (active) {
            for (entity in player.level().getEntities(null, player.boundingBox.inflate(2.0))) {
                if (entity !== player) {
                    entity.deltaMovement =
                        player.position().subtract(entity.position()).normalize().scale(2.0).reverse()
                    if (entity is LivingEntity) {
                        entity.hurt(player.damageSources().playerAttack(player), 10f)
                    }
                }
            }
            if (activeTicks > 60) {
                active = false
                resetCoolDown()
            }
        }
    }
}


