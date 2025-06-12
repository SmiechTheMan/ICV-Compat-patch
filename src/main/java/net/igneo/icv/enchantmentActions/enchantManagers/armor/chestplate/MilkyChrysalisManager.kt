package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class MilkyChrysalisManager(player: Player?) :
    ArmorEnchantManager(EnchantType.CHESTPLATE, 300, -10, true, player) {
    var position: Vec3? = null
    private val blackList: List<Entity> = ArrayList()

    override fun activate() {
        println("activating")
        player.deltaMovement = ICVUtils.getFlatInputDirection(player.yRot, enchVar!!.input, 1.2f, 1.0)
        player.startFallFlying()
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
        active = false
        activeTicks = 0
        resetCoolDown()
    }

    override var isDualUse: Boolean
        get() = true
        set(isDualUse) {
            super.isDualUse = isDualUse
        }

    override fun tick() {
        super.tick()
        if (active) {
            player.startFallFlying()
            ++activeTicks
            if (activeTicks > 1000) {
                dualActivate()
            }
        }
    }
}


