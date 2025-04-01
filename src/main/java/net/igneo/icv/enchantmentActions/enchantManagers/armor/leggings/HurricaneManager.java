package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HurricaneManager extends ArmorEnchantManager {
    public HurricaneManager(Player player) {
        super(EnchantType.LEGGINGS, 300, -10, false, player);
    }
    @Override
    public void activate() {
        System.out.println("activating");
        player.setDeltaMovement(ICVUtils.getFlatDirection(player.getYRot(),2,0.5));
        active = true;
    }

    @Override
    public void onOffCoolDown(Player player) {

    }

    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }

    @Override
    public boolean canUse() {
        return !active;
    }

    @Override
    public void tick() {
        super.tick();
        if (active) {
            for (Entity entity : player.level().getEntities(null,player.getBoundingBox().inflate(2))) {
                if (entity != player) {
                    entity.setDeltaMovement(player.position().subtract(entity.position()).normalize().scale(2).reverse());
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.hurt(player.damageSources().playerAttack(player), 10);
                    }
                }
            }
            if (activeTicks > 60) {
                active = false;
                resetCoolDown();
            }
        }
    }
}


