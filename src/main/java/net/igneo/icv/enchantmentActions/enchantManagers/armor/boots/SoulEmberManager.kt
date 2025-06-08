package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots

import net.igneo.icv.client.indicators.BlackHoleIndicator
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.init.ICVUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

class SoulEmberManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 300, -30, false, player), EntityTracker {
    override var child: ICVEntity? = null

    override fun tick() {
        super.tick()
        if (child != null && !child!!.isAlive) {
            child = null
        }
    }

    override fun canUse(): Boolean {
        return child == null
    }

    override val indicator: EnchantIndicator
        get() = BlackHoleIndicator(this)

    override fun onOffCoolDown(player: Player?) {
    }

    override fun shouldTickCooldown(): Boolean {
        return child == null
    }

    override fun activate() {
        if (player!!.level() is ServerLevel) {
            child = ModEntities.SOUL_EMBER.get().create(player!!.level())
            child!!.owner = player
            child!!.setPos(player!!.eyePosition.subtract(0.0, 0.7, 0.0))
            child!!.deltaMovement = ICVUtils.getFlatDirection(player!!.yRot, 2f, 0.0)
            player!!.level().addFreshEntity(child!!)
            syncClientChild(player as ServerPlayer, child, this)
        }
    }

    override fun onRemove() {
        if (this.child != null) child!!.discard()
    }
}
