package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate

import net.igneo.icv.Utils.getFlatDirection
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.SOUL_ORB
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

class HauntManager(player: Player?) :
    ArmorEnchantManager(EnchantType.CHESTPLATE, 300, -10, true, player), EntityTracker {
    override var child: ICVEntity? = null
    private var nullCheck = false

    override fun activate() {
        val level = player.level()
        if (player.level() is ServerLevel) {
            child = SOUL_ORB.get().create(player.level())
            child!!.owner = player
            child!!.setPos(player.eyePosition.subtract(0.0, 1.0, 0.0))
            child!!.deltaMovement = getFlatDirection(player.yRot, 1f, 0.0)
            level.addFreshEntity(child)
            syncClientChild(player as ServerPlayer, child, this)
            nullCheck = true
        }
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun canUse(): Boolean {
        return child == null
    }

    override fun dualActivate() {
        child!!.setPos(player.eyePosition)
    }

    override var isDualUse: Boolean
        get() = true
        set(isDualUse) {
            super.isDualUse = isDualUse
        }


    override fun tick() {
        super.tick()
        if (nullCheck && player is ServerPlayer && child == null) {
            syncClientChild(player as ServerPlayer, null, this)
            nullCheck = false
        }
    }
}


