package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CurbStompManager extends ArmorEnchantManager {
    private boolean persist;
    private boolean primed;
    
    public CurbStompManager(Player player) {
        super(EnchantType.BOOTS, 300, 10, false, player);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }
    
    @Override
    public boolean canUse() {
        return !active;
    }
    
    @Override
    public void activate() {
        if (!player.onGround() && player.fallDistance > 1) {
            primed = true;
            player.setDeltaMovement(0, -1, 0);
        } else {
            persist = true;
            player.setDeltaMovement(0, 1, 0);
        }
        active = true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (active) {
            ++activeTicks;
        } else {
            activeTicks = 0;
        }
        if (activeTicks > 60 && !player.onGround() && persist) {
            persist = false;
            primed = true;
            player.setDeltaMovement(0, -1, 0);
        }
        if (primed && player.onGround()) {
            for (Entity e : ICVUtils.collectEntitiesBox(player.level(), player.position(), 3)) {
                if (e != player && e instanceof LivingEntity entity) {
                    entity.hurt(player.damageSources().playerAttack(player), entity.getDeltaMovement().length() < 0.1 ? 20 : 10);
                }
            }
            resetCoolDown();
            primed = false;
            persist = false;
            active = false;
            activeTicks = 0;
        }
    }
    
    @Override
    public void resetCoolDown() {
        super.resetCoolDown();
        System.out.println("resettin");
        
    }
}