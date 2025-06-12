package net.igneo.icv.Utils

import net.igneo.icv.enchantment.ICVEnchantment
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.Enchantment

fun <T : EnchantmentManager> getManagerForType(
    player: Player,
    desiredManager: Class<T>
): T? {
    return player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
        .map { enchVar ->
            (enchVar.managers
                .filterNotNull()
                .firstOrNull { it::class.java == desiredManager } as? T)!!
        }
        .orElse(null)
}

fun <T : EnchantmentManager> getSlotForType(
    player: Player,
    desiredManager: Class<T>
): Int {
    return player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
        .map { enchVar ->
            enchVar.managers
                .indexOfFirst { it != null && it::class.java == desiredManager }
        }
        .orElse(-1)
}

fun directUpdate(slot: Int) {
    Minecraft.getInstance().player?.let { player ->
        updateManager(player, slot)
    }
}

fun syncClientEntity(entityId: Int, slot: Int) {
    val player = Minecraft.getInstance().player ?: return
    val level = Minecraft.getInstance().level ?: return

    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
        .ifPresent { enchVar ->
            val manager = enchVar.getManager(slot) as? EntityTracker ?: return@ifPresent

            manager.child = when (entityId) {
                -1 -> null
                else -> {
                    val entity = level.getEntity(entityId) as? ICVEntity
                    entity?.apply { owner = player }
                }
            }
        }
}

fun clientCooldownDamageBonuses() {
    Minecraft.getInstance().player?.let(::sendCooldownDamageBonuses)
}

fun updateManager(player: Player, slotIndex: Int) {
    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
        .ifPresent { enchVar ->
            val normalizedSlot = normalizeSlotIndex(slotIndex)
            val enchantments = getEnchantsForSlot(player, normalizedSlot)

            val manager = enchantments
                .filterIsInstance<ICVEnchantment>()
                .firstOrNull()
                ?.getManager(player)

            enchVar.setManager(manager, normalizedSlot)
        }
}

private fun normalizeSlotIndex(slotIndex: Int): Int = when {
    slotIndex == 0 -> 4
    slotIndex <= 4 -> slotIndex - 1
    else -> slotIndex
}

private fun getEnchantsForSlot(player: Player, slot: Int): List<Enchantment> = when (slot) {
    in 0..3 -> player.inventory.getArmor(slot).allEnchantments.keys.toList()
    4 -> player.mainHandItem.allEnchantments.keys.toList()
    5 -> player.offhandItem.allEnchantments.keys.toList()
    else -> emptyList()
}

fun useEnchant(player: Player, slot: Int) {
    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
        .ifPresent { enchVar ->
            enchVar.getManager(slot)?.use()
        }
}

fun sendCooldownDamageBonuses(player: Player) {
    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
        .ifPresent { enchVar ->
            enchVar.managers
                .filterIsInstance<ArmorEnchantManager>()
                .forEach { it.targetDamaged() }
        }
}