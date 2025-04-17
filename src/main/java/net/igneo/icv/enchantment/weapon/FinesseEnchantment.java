package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.FinesseManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FinesseEnchantment extends WeaponEnchantment {
    public FinesseEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    
    @Override
    public EnchantmentManager getManager(Player player) {
        return new FinesseManager(player);
    }
}
