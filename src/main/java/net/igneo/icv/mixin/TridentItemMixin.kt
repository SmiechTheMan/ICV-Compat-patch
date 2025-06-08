package net.igneo.icv.mixin

import net.igneo.icv.enchantment.ICVEnchantment
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.trident.TridentEnchantManager
import net.igneo.icv.init.ICVUtils.getManagerForType
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TridentItem
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(value = [TridentItem::class])
class TridentItemMixin {
    @Inject(method = ["releaseUsing"], at = [At("HEAD")], cancellable = true)
    fun releaseUsing(pStack: ItemStack, pLevel: Level?, pEntityLiving: LivingEntity, pTimeLeft: Int, ci: CallbackInfo) {
        if (pEntityLiving is Player) {
            for (enchantment in pStack.allEnchantments.keys) {
                if (enchantment is ICVEnchantment) {
                    pEntityLiving.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                        .ifPresent {
                            val manager = enchantment.getManager(pEntityLiving)!!.javaClass as TridentEnchantManager
                            if (getManagerForType(
                                    pEntityLiving,
                                    enchantment.getManager(pEntityLiving)!!.javaClass
                                ) is TridentEnchantManager
                            ) {
                                manager.onRelease()
                            }
                        }
                }
            }
        }
        ci.cancel()
    }

    @Inject(method = ["use"], at = [At("HEAD")], cancellable = true)
    private fun use(
        level: Level,
        player: Player,
        hand: InteractionHand,
        cir: CallbackInfoReturnable<InteractionResultHolder<ItemStack>>
    ) {
        val pStack = player.getItemInHand(hand)
        val trident = level.getEntity(pStack.tag!!.getInt("tridentID")) as ThrownTrident?
        if (!(trident == null || !trident.isAddedToWorld)) {
            cir.setReturnValue(InteractionResultHolder.success(pStack))
        } else {
            val throwntrident = ThrownTrident(level, player, pStack)
            throwntrident.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 2.5f, 1.0f)

            level.addFreshEntity(throwntrident)
            level.playSound(null, throwntrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0f, 1.0f)
            pStack.tag!!.putInt("tridentID", throwntrident.id)
            cir.setReturnValue(InteractionResultHolder.success(pStack))
        }
        cir.cancel()
    }
}
