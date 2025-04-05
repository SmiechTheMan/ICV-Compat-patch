package net.igneo.icv.enchantment.trident;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.trident.WhirlpoolManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WhirlpoolEnchantment extends ICVEnchantment {
    public WhirlpoolEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    
    @Override
    public EnchantmentManager getManager(Player player) {
        return new WhirlpoolManager(player);
    }
}
