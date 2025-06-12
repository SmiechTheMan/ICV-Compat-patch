package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.trident.TridentEnchantManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin (value = ThrownTrident.class)
public abstract class TridentEntityMixin extends AbstractArrow {
    private final ThrownTrident instance = (ThrownTrident) (Object) this;
    private TridentEnchantManager manager;
    @Shadow
    private ItemStack tridentItem;
    
    protected TridentEntityMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Inject (method = "tick", at = @At ("TAIL"))
    public void tick(CallbackInfo ci) {
        if (manager == null) {
            for (Enchantment enchantment : tridentItem.getAllEnchantments().keySet()) {
                System.out.println("getting enchantments...");
                if (enchantment instanceof ICVEnchantment enchant && instance.getOwner() instanceof Player player) {
                    System.out.println("SUCCESS! getting capability...");
                    player.getCapability(PlayerEnchantmentActionsProvider.Companion.getPLAYER_ENCHANTMENT_ACTIONS()).ifPresent(enchVar -> {
                        System.out.println("SUCCESS!! getting manager from: " + Arrays.stream(enchVar.managers).toList());
                        for (EnchantmentManager manager : enchVar.managers) {
                            if (manager instanceof TridentEnchantManager tManager) {
                                this.manager = tManager;
                                System.out.println("OPERATION SUCCESS");
                                break;
                            }
                        }
                        if (manager == null) {
                            System.out.println("FAILED");
                        }
                    });
                } else {
                    System.out.println("FAILED");
                }
            }
        }
    }
    
    
    @Inject (method = "tryPickup", at = @At ("HEAD"), cancellable = true)
    protected void tryPickup(Player pPlayer, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("gamer");
        instance.discard();
        cir.setReturnValue(false);
    }
    
    @Inject (method = "onHitEntity", at = @At ("HEAD"), cancellable = true)
    protected void onHitEntity(EntityHitResult pResult, CallbackInfo ci) {
        if (manager != null) {
            manager.onHitEntity(pResult, instance);
            if (manager.overrideOnHitEntity()) ci.cancel();
        }
    }
    
    @Unique
    @Override
    protected void onHit(HitResult pResult) {
        if (manager != null) {
            manager.onHit(pResult, instance);
            if (!manager.overrideOnHit()) super.onHit(pResult);
        } else {
            super.onHit(pResult);
        }
    }
    
    @Unique
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (manager != null) {
            manager.onHitBlock(pResult, instance);
            if (!manager.overrideOnHitBlock()) super.onHitBlock(pResult);
        } else {
            super.onHitBlock(pResult);
        }
    }
    
}
