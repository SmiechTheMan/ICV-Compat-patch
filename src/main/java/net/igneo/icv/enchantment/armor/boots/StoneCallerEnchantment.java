package net.igneo.icv.enchantment.armor.boots;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.StoneCallerManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class StoneCallerEnchantment extends ICVEnchantment {
    public StoneCallerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    
    @Override
    public EnchantmentManager getManager(Player player) {
        return new StoneCallerManager(player);
    }
}
