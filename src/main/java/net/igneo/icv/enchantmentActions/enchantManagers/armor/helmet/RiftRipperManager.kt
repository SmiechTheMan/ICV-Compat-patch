package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class RiftRipperManager(player: Player?) :
    ArmorEnchantManager(EnchantType.HELMET, 300, -10, true, player) {
    private var oldPlayerPosition: Vec3? = null
    private var newPlayerPosition: Vec3? = null
    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun activate() {
        println("running activate")
        oldPlayerPosition = player!!.position()
        active = true
    }

    override fun canUse(): Boolean {
        return !active
    }

    override fun dualActivate() {
        println(oldPlayerPosition)
        newPlayerPosition = player!!.position()
        resetCoolDown()
        newPlayerPosition = player!!.position()

        player!!.setPos(oldPlayerPosition)

        val entities = oldPlayerPosition?.let { ICVUtils.collectEntitiesBox(player!!.level(), it, TELEPORT_RADIUS) }

        for (entity in entities!!) {
            entity.setPos(newPlayerPosition)
        }

        active = false
        resetCoolDown()
    }

    override var isDualUse: Boolean
        get() = true
        set(isDualUse) {
            super.isDualUse = isDualUse
        }

    companion object {
        private const val TELEPORT_RADIUS = 5.0
    }
}
