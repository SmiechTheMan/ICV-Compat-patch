package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class BreakthroughManager extends WeaponEnchantManager {
    public BreakthroughManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
    }
    
    @Override
    public void activate() {
        super.activate();
        ICVEntity closestEntity = null;
        double lowestDistance = 99999;
        for (Entity e : ICVUtils.collectEntitiesBox(player.level(), player.position().add(player.getLookAngle().scale(0.3)), 2)) {
            if (e instanceof ICVEntity entity) {
                if (player.distanceTo(entity) < lowestDistance) {
                    closestEntity = entity;
                    lowestDistance = player.distanceTo(entity);
                }
            }
        }
        if (closestEntity != null) closestEntity.discard();
    }
}
