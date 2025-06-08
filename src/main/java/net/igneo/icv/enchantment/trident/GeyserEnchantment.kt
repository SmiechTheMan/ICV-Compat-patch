package net.igneo.icv.enchantment.trident

import net.igneo.icv.enchantment.ICVEnchantment
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.trident.GeyserManager
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.EnchantmentCategory

class GeyserEnchantment(pRarity: Rarity?, pCategory: EnchantmentCategory?, vararg pApplicableSlots: EquipmentSlot?) :
    ICVEnchantment(pRarity, pCategory, pApplicableSlots) {
    override fun getManager(player: Player?): EnchantmentManager {
        return GeyserManager(player)
    }
}
