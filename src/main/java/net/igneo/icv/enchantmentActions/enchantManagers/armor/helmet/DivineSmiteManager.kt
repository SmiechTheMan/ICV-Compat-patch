package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class DivineSmiteManager(player: Player?) :
    ArmorEnchantManager(EnchantType.HELMET, 300, -10, false, player), EntityTracker {
    override var child: ICVEntity? = null

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun activate() {
        val level = player!!.level()
        if (player!!.level() !is ServerLevel) {
            return
        }

        val lightningRod = ModEntities.DIVINE_LIGHTNING_ROD.get().create(level) ?: return

        lightningRod.owner = player

        val hitResult = player!!.pick(RANGE.toDouble(), 0f, false)

        if (hitResult.type != HitResult.Type.BLOCK) {
            return
        }

        val blockHitResult = hitResult as BlockHitResult
        val position = blockHitResult.location

        lightningRod.setPos(position.x, position.y, position.z)

        val immuneEntities: MutableList<EntityType<*>> = ArrayList()
        immuneEntities.add(player!!.type)

        player!!.level().addFreshEntity(lightningRod)

        child = lightningRod

        if (player is ServerPlayer) {
            syncClientChild(player as ServerPlayer, child, this)
        }
    }

    override fun tick() {
        super.tick()

        if (child != null && !child!!.isAlive) {
            child = null
        }
    }

    override fun canUse(): Boolean {
        return child == null
    }

    override fun shouldTickCooldown(): Boolean {
        return child == null
    }

    override fun onRemove() {
        if (this.child != null) {
            child!!.discard()
        }
    }

    companion object {
        private const val RANGE = 20
    }
}