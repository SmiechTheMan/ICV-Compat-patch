package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ABYSS_STONE
import net.igneo.icv.entity.ICVEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult

class AbyssOmenManager(player: Player?) :
    ArmorEnchantManager(EnchantType.CHESTPLATE, 300, -10, false, player), EntityTracker {
    override var child: ICVEntity? = null

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun activate() {
        val level = player.level()
        if (player.level() !is ServerLevel) {
            return
        }
        child = ABYSS_STONE.get().create(player.level())
        child!!.owner = player

        val hitResult = player.pick(5.0, 0f, false)

        val blockHitResult = hitResult as BlockHitResult
        val position = blockHitResult.location

        child!!.setPos(position)
        level.addFreshEntity(child)
        syncClientChild(player as ServerPlayer, child, this)
    }

    override fun shouldTickCooldown(): Boolean {
        return child == null
    }

    override fun tick() {
        super.tick()
        if (child != null && !child!!.isAlive) {
            child = null
        }
    }

    override fun onRemove() {
        if (this.child != null) {
            child!!.discard()
        }
    }

    override fun canUse(): Boolean {
        return stableCheck() && child == null
    }
}