package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet

import net.igneo.icv.client.indicators.BlackHoleIndicator
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class BlackHoleManager(player: Player?) :
    ArmorEnchantManager(EnchantType.HELMET, 900, -30, true, player), EntityTracker {
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
            child = ModEntities.BLACK_HOLE.get().create(player!!.level())
            child!!.owner = player
            child!!.setPos(player!!.eyePosition)
            child!!.deltaMovement = player!!.lookAngle.scale(0.4)
            player!!.level().addFreshEntity(child)
            syncClientChild(player as ServerPlayer, child, this)
        }
    }

    override fun dualActivate() {
        if (child != null && player!!.level() is ServerLevel) {
            val pushVec = Vec3((player!!.x - child!!.x), ((player!!.eyeY - 0.5) - child!!.y), (player!!.z - child!!.z))
            if (child!!.deltaMovement.length() < 0.3) {
                child!!.addDeltaMovement(pushVec.normalize().scale(0.1))
            } else {
                child!!.deltaMovement = pushVec.normalize().scale(0.3)
            }
        }
    }

    override fun onRemove() {
        if (this.child != null) child!!.discard()
    }
}
