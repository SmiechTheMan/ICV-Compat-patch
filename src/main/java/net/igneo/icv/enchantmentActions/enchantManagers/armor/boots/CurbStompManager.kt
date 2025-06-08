package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots

import net.igneo.icv.client.indicators.BlackHoleIndicator
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class CurbStompManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 300, 10, false, player) {
    private var persist = false
    private var primed = false

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = BlackHoleIndicator(this)

    override fun canUse(): Boolean {
        return !active
    }

    override fun activate() {
        if (!player!!.onGround() && player!!.fallDistance > 1) {
            primed = true
            player!!.setDeltaMovement(0.0, -1.0, 0.0)
        } else {
            persist = true
            player!!.setDeltaMovement(0.0, 1.0, 0.0)
        }
        active = true
    }

    override fun tick() {
        super.tick()
        if (active) {
            ++activeTicks
        } else {
            activeTicks = 0
        }
        if (activeTicks > 60 && !player?.onGround()!! && persist) {
            persist = false
            primed = true
            player!!.setDeltaMovement(0.0, -1.0, 0.0)
        }
        if (primed && player!!.onGround()) {
            for (e in ICVUtils.collectEntitiesBox(player!!.level(), player!!.position(), 3.0)) {
                if (e !== player && e is LivingEntity) {
                    e.hurt(
                        player!!.damageSources().playerAttack(player),
                        (if (e.getDeltaMovement().length() < 0.1) 20 else 10).toFloat()
                    )
                }
            }
            resetCoolDown()
            primed = false
            persist = false
            active = false
            activeTicks = 0
        }
    }

    override fun resetCoolDown() {
        super.resetCoolDown()
        println("resettin")
    }
}