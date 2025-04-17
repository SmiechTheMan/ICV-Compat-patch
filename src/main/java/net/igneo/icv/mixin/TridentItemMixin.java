package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.trident.TridentEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = TridentItem.class)
public class TridentItemMixin {

    @Inject(method = "releaseUsing" , at= @At("HEAD"), cancellable = true)
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci) {
        if (pEntityLiving instanceof Player player) {
            for (Enchantment enchantment : pStack.getAllEnchantments().keySet()) {
                if (enchantment instanceof ICVEnchantment enchant) {
                    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                        if (ICVUtils.getManagerForType(player, enchant.getManager(player).getClass()) instanceof TridentEnchantManager manager) {
                            manager.onRelease();
                        }
                    });
                }
            }
        }
        ci.cancel();
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack pStack = player.getItemInHand(hand);
        ThrownTrident trident = (ThrownTrident) level.getEntity(pStack.getTag().getInt("tridentID"));
        if (!(trident == null || !trident.isAddedToWorld())) {
            cir.setReturnValue(InteractionResultHolder.success(pStack));
        } else {
            ThrownTrident throwntrident = new ThrownTrident(level, player, pStack);
            throwntrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);

            level.addFreshEntity(throwntrident);
            level.playSound((Player) null, throwntrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            pStack.getTag().putInt("tridentID",throwntrident.getId());
            cir.setReturnValue(InteractionResultHolder.success(pStack));
        }
        cir.cancel();
    }
}
