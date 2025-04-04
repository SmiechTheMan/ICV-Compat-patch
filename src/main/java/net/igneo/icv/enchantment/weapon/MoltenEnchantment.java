package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.MoltenManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.TungstenCoreManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Set;

public class MoltenEnchantment extends WeaponEnchantment{
    public MoltenEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public boolean performBonusCheck() {
        return true;
    }

    @Override
    public EnchantmentManager getManager(Player player) {
        return new MoltenManager(player);
    }

    @Override
    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories) {
        return super.allowedInCreativeTab(book, allowedCategories);
    }
}
