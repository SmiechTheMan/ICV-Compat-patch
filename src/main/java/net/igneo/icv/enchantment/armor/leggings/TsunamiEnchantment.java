package net.igneo.icv.enchantment.armor.leggings;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings.GaleManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings.TsunamiManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TsunamiEnchantment extends ICVEnchantment {
  public TsunamiEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
    super(pRarity, pCategory, pApplicableSlots);
  }
  
  @Override
  public EnchantmentManager getManager(Player player) {
    return new TsunamiManager(player);
  }
}
