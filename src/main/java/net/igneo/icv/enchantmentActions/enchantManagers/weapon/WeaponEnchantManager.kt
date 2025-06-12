package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import net.igneo.icv.ICV
import net.igneo.icv.client.animation.EnchantAnimationPlayer
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

abstract class WeaponEnchantManager protected constructor(
    setSlot: EnchantType,
    player: Player,
    animationRes: ResourceLocation
) : EnchantmentManager(setSlot, player) {

    @OnlyIn(Dist.CLIENT)
    var animationPlayer: KeyframeAnimationPlayer? = null

    var target: Entity? = null

    init {
        if (player is LocalPlayer) {
            val clientPlayer = player as? AbstractClientPlayer
            animator = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer!!)[
                ResourceLocation(ICV.MOD_ID, "enchant_animator")
            ] as? ModifierLayer<IAnimation>

            val animation = PlayerAnimationRegistry.getAnimation(animationRes)
            animationPlayer = animation?.let { KeyframeAnimationPlayer(it) }
        }
    }

    open fun onAttack(entity: Entity?) {
        // Override to add attack behavior
    }

    override fun canUse(): Boolean = stableCheck()

    override fun tick() {
        super.tick()
        // Custom tick behavior can be added here
    }

    override fun use() {
        if (canUse()) activate()
    }

    open fun applyPassive() {
        // Override to add passive effect
    }

    open fun removePassive() {
        // Override to remove passive effect
    }

    override fun onEquip() {

    }

    override fun activate() {
        requireNotNull(enchVar).animated = true

        if (player.level().isClientSide) {
            println("Activating animation...")

            val enchantAnim = PlayerAnimationRegistry.getAnimation(
                ResourceLocation(ICV.MOD_ID, "comet_strike")
            )

            animator?.animation = enchantAnim?.let { EnchantAnimationPlayer(it) }
        }

        active = true
    }

    open val damageBonus: Float
        get() = 0f
}
