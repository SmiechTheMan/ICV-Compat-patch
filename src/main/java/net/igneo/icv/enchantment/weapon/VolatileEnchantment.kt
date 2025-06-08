package net.igneo.icv.enchantment.weapon

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.VolatileManager
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.EnchantmentCategory

class VolatileEnchantment(pRarity: Rarity?, pCategory: EnchantmentCategory?, vararg pApplicableSlots: EquipmentSlot?) :
    WeaponEnchantment(pRarity, pCategory, pApplicableSlots) {
    override fun getManager(player: Player?): EnchantmentManager {
        return VolatileManager(player)
    }
}
