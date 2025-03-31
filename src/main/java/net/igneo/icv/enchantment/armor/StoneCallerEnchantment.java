package net.igneo.icv.enchantment.armor;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.BlinkManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.StoneCallerManager;
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
