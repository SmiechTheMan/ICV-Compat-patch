package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class GaleManager extends ArmorEnchantManager {
    public GaleManager(Player player) {
        super(EnchantType.LEGGINGS, 300, -10, false, player);
    }
    
    @Override
    public void activate() {
        System.out.println("activating");
        for (Entity entity : player.level().getEntities(null, player.getBoundingBox().inflate(5))) {
            float scale = 2;
            if (entity instanceof LivingEntity) {
                scale = 4;
            }
            entity.setDeltaMovement(ICVUtils.getFlatDirection(player.getYRot(), scale, 0.5));
        }
        resetCoolDown();
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
}


