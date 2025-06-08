package net.igneo.icv.enchantment.tool

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory

class BruteTouchEnchantment(pRarity: Rarity, pCategory: EnchantmentCategory, vararg pApplicableSlots: EquipmentSlot?) :
    Enchantment(pRarity, pCategory, pApplicableSlots)
