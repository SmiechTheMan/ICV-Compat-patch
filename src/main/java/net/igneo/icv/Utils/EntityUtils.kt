package net.igneo.icv.Utils

import net.igneo.icv.networking.sendToPlayer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

fun collectEntitiesBox(level: Level, position: Vec3, radius: Double): List<Entity> {
    val scale = Vec3(radius, radius, radius)
    return level.getEntities(null, AABB(position.subtract(scale), position.add(scale)))
}

fun sendPacketInRange(level: ServerLevel, pos: Vec3, range: Float, message: Any) {
    val rangeSquared = range * range
    level.players()
        .filter { it.distanceToSqr(pos) < rangeSquared }
        .forEach { sendToPlayer(message, it) }
}

fun getItemForSlot(player: Player, slot: Int): ItemStack = when (slot) {
    in 1..3 -> player.inventory.getArmor(slot)
    4 -> player.mainHandItem
    5 -> player.offhandItem
    else -> player.inventory.getArmor(0)
}

fun getDamageFromItem(item: ItemStack, entity: Entity): Double {
    val damageModifiers = item.getAttributeModifiers(EquipmentSlot.MAINHAND)[Attributes.ATTACK_DAMAGE]
    val baseDamage = damageModifiers.sumOf { it.amount }

    return when (entity) {
        is Player -> baseDamage + entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE)
        else -> baseDamage
    }
}