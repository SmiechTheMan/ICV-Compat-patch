package net.igneo.icv.enchantmentActions.enchantManagers

import com.alrex.parcool.common.action.impl.Dodge
import com.alrex.parcool.common.action.impl.Slide
import com.alrex.parcool.common.capability.Parkourability
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.AnimatedSyncC2SPacket
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

abstract class EnchantmentManager protected constructor(val type: EnchantType, var player: Player) {
    @JvmField
    @OnlyIn(Dist.CLIENT)
    var animator: ModifierLayer<IAnimation>? = null
    var activeTicks: Int = 0
    var active: Boolean = false

    var enchVar: PlayerEnchantmentActions? = null

    init {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions -> this.enchVar = enchVar }
        if (player is LocalPlayer) {
            this.animator =
                PlayerAnimationAccess.getPlayerAssociatedData(player as AbstractClientPlayer)[ResourceLocation(
                    ICV.MOD_ID,
                    "enchant_animator"
                )] as ModifierLayer<IAnimation>
        }
    }

    open fun use() {
        this.activate()
    }

    abstract fun canUse(): Boolean

    abstract fun activate()

    open fun onRemove() {
    }

    open fun onEquip() {
    }

    open fun tick() {
        if (active) {
            ++activeTicks
        } else {
            activeTicks = 0
        }
        if (player.level().isClientSide) {
            if (!animator!!.isActive && enchVar!!.animated) {
                enchVar?.animated = false
                ModMessages.sendToServer(AnimatedSyncC2SPacket(false))
            }
        }
    }

    open fun stableCheck(): Boolean {
        return player.onGround() &&
                !enchVar!!.animated &&
                !player.isSwimming &&
                !Parkourability.get(player)!!.get(Dodge::class.java).isDoing &&
                !Parkourability.get(player)!!.get(Slide::class.java).isDoing
    }
}
