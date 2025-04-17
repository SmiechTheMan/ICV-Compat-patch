package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.weapon.WeaponEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.BreakthroughManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (value = Player.class)
public abstract class PlayerMixin extends LivingEntity {
    
    private double shieldHealth = 30;
    private final int checkTicks = 0;
    
    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Shadow
    public void disableShield(boolean pBecauseOfAxe) {
    
    }
    
    @Redirect (method = "tick", at = @At (value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    public void tick(Player instance) {
    
    }
    
    @Inject (method = "attack", at = @At ("HEAD"), cancellable = true)
    private void attackHead(Entity pTarget, CallbackInfo ci) {
        this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (Enchantment firstEnchantment : this.getMainHandItem().getAllEnchantments().keySet()) {
                if (firstEnchantment instanceof WeaponEnchantment enchantment && this.level() instanceof ServerLevel level) {
                    enchantment.currentLevel = level;
                    if (enchVar.getManager(4) != null && enchVar.getManager(4) instanceof WeaponEnchantManager manager) {
                        manager.target = pTarget;
                    }
                    if (enchVar.getManager(5) != null && enchVar.getManager(5) instanceof WeaponEnchantManager manager) {
                        manager.target = pTarget;
                    }
                }
            }
        });
    }
    
    @Inject (method = "blockUsingShield", at = @At ("HEAD"), cancellable = true)
    protected void blockUsingShield(LivingEntity pEntity, CallbackInfo ci) {
        super.blockUsingShield(pEntity);
        double damage = ICVUtils.getDamageFromItem(pEntity.getMainHandItem(), pEntity);
        damage = (damage < 5) ? 5 : damage;
        shieldHealth -= damage;
        
        if (pEntity instanceof Player player) {
            if (ICVUtils.getManagerForType(player, BreakthroughManager.class) != null) {
                shieldHealth = 0;
            }
        }
        
        if (shieldHealth <= 0) {
            shieldHealth = 30;
            this.disableShield(true);
        }
        ci.cancel();
    }
}
