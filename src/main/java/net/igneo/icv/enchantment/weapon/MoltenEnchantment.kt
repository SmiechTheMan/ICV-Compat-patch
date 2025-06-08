package net.igneo.icv.enchantment.weapon

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.MoltenManager
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.EnchantmentCategory

class MoltenEnchantment(pRarity: Rarity?, pCategory: EnchantmentCategory?, vararg pApplicableSlots: EquipmentSlot?) :
    WeaponEnchantment(pRarity, pCategory, pApplicableSlots) {
    override fun performBonusCheck(): Boolean {
        return true
    }

    override fun getManager(player: Player?): EnchantmentManager {
        return MoltenManager(player)
    }

    override fun allowedInCreativeTab(book: Item, allowedCategories: Set<EnchantmentCategory>): Boolean {
        return super.allowedInCreativeTab(book, allowedCategories)
    }
}
