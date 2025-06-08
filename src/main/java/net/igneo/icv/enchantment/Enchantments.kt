package net.igneo.icv.enchantment

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.*
import net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate.*
import net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet.*
import net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings.*
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.BreakthroughManager
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager
import net.igneo.icv.networking.ModMessages.sendToPlayer
import net.igneo.icv.networking.packet.EnchantAttackS2CPacket
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraft.world.level.Level
import net.minecraftforge.server.ServerLifecycleHooks

abstract class ICVEnchantment(
    open val rarity: Rarity,
    open val category: EnchantmentCategory,
    open val applicableSlots: Array<out EquipmentSlot>
) : Enchantment(rarity, category, applicableSlots) {
    abstract fun getManager(player: Player): EnchantmentManager
}

abstract class WeaponEnchantment(
    override val rarity: Rarity,
    override val category: EnchantmentCategory,
    override val applicableSlots: Array<out EquipmentSlot>
) : ICVEnchantment(rarity, category, applicableSlots) {
    private var currentLevel: ServerLevel? = null

    override fun doPostAttack(attacker: LivingEntity, target: Entity, level: Int) {
        if (attacker.level().isClientSide || attacker !is Player) return

        val serverLevel = attacker.level() as? ServerLevel ?: return
        currentLevel = serverLevel

        attacker.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent { enchVar ->
            val currentManagerClass = getManager(attacker).javaClass

            listOf(4, 5).forEach { index ->
                val manager = enchVar.getManager(index)
                if (manager?.javaClass == currentManagerClass && manager is WeaponEnchantManager) {
                    manager.onAttack(target)
                    sendToPlayer(EnchantAttackS2CPacket(index, target.id), attacker as ServerPlayer)
                    return@forEach
                }
            }
        }
    }

    override fun getDamageBonus(power: Int, mobType: MobType, enchantedItem: ItemStack): Float {
        if (!performBonusCheck()) return 0f

        val level = currentLevel ?: return 0f

        for (player in level.players()) {
            val enchVar = player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .resolve()
                .orElse(null) ?: continue

            val mainManager = enchVar.getManager(4)
            val offManager = enchVar.getManager(5)

            val isMain = player.mainHandItem == enchantedItem && mainManager is WeaponEnchantManager
            val isOff = player.offhandItem == enchantedItem && offManager is WeaponEnchantManager

            val manager = when {
                isMain -> mainManager as WeaponEnchantManager
                isOff -> offManager as WeaponEnchantManager
                else -> continue
            }

            return manager.damageBonus
        }

        return 0f
    }

    open fun performBonusCheck(): Boolean = false

    companion object {
        fun getServerLevel(dimension: ResourceKey<Level?>): ServerLevel? {
            return ServerLifecycleHooks.getCurrentServer()?.getLevel(dimension)
        }
    }

}


// Helmets

class BlackHoleEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_HEAD,
    applicableSlots = arrayOf(EquipmentSlot.HEAD)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return BlackHoleManager(player)
    }
}

class DivineSmiteEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_HEAD,
    applicableSlots = arrayOf(EquipmentSlot.HEAD)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return DivineSmiteManager(player)
    }
}

class GlacialImpasseEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_HEAD,
    applicableSlots = arrayOf(EquipmentSlot.HEAD)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return GlacialImpasseManager(player)
    }
}

class GravityWellEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_HEAD,
    applicableSlots = arrayOf(EquipmentSlot.HEAD)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return GravityWellManager(player)
    }
}

class RiftRipperEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_HEAD,
    applicableSlots = arrayOf(EquipmentSlot.HEAD)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return RiftRipperManager(player)
    }
}

class VolcanoEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_HEAD,
    applicableSlots = arrayOf(EquipmentSlot.HEAD)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return VolcanoManager(player)
    }
}

// Chestplates

class AbyssOmenEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_CHEST,
    applicableSlots = arrayOf(EquipmentSlot.CHEST)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return AbyssOmenManager(player)
    }
}

class ExtinctionEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_CHEST,
    applicableSlots = arrayOf(EquipmentSlot.CHEST)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return ExtinctionManager(player)
    }
}

class HauntEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_CHEST,
    applicableSlots = arrayOf(EquipmentSlot.CHEST)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return HauntManager(player)
    }
}

class ImmolateEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_CHEST,
    applicableSlots = arrayOf(EquipmentSlot.CHEST)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return ImmolateManager(player)
    }
}

class MilkyChrysalisEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_CHEST,
    applicableSlots = arrayOf(EquipmentSlot.CHEST)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return MilkyChrysalisManager(player)
    }
}

class PlanarShiftEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_CHEST,
    applicableSlots = arrayOf(EquipmentSlot.CHEST)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return PlanarShiftManager(player)
    }
}

// Leggings

class GaleEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_LEGS,
    applicableSlots = arrayOf(EquipmentSlot.LEGS)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return GaleManager(player)
    }
}

class HurricaneEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_LEGS,
    applicableSlots = arrayOf(EquipmentSlot.LEGS)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return HurricaneManager(player)
    }
}

class JudgementEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_LEGS,
    applicableSlots = arrayOf(EquipmentSlot.LEGS)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return JudgementManager(player)
    }
}

class TempestEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_LEGS,
    applicableSlots = arrayOf(EquipmentSlot.LEGS)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return TempestManager(player)
    }
}

class TsunamiEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_LEGS,
    applicableSlots = arrayOf(EquipmentSlot.LEGS)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return TsunamiManager(player)
    }
}

class VoidWakeEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_LEGS,
    applicableSlots = arrayOf(EquipmentSlot.LEGS)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return VoidWakeManager(player)
    }
}


// Boots

class BlinkEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_FEET,
    applicableSlots = arrayOf(EquipmentSlot.FEET)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return BlinkManager(player)
    }
}

class CurbStompEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_FEET,
    applicableSlots = arrayOf(EquipmentSlot.FEET)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return CurbStompManager(player)
    }
}

class SoulEmberEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_FEET,
    applicableSlots = arrayOf(EquipmentSlot.FEET)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return SoulEmberManager(player)
    }
}

class StasisEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_FEET,
    applicableSlots = arrayOf(EquipmentSlot.FEET)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return StasisManager(player)
    }
}

class StoneCallerEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_FEET,
    applicableSlots = arrayOf(EquipmentSlot.FEET)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return StoneCallerManager(player)
    }
}

class SurfEnchantment : ICVEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.ARMOR_FEET,
    applicableSlots = arrayOf(EquipmentSlot.FEET)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return SurfManager(player)
    }
}

// Weapons

class BreakthroughEnchantment : WeaponEnchantment(
    rarity = Rarity.UNCOMMON,
    category = EnchantmentCategory.WEAPON,
    applicableSlots = arrayOf(EquipmentSlot.MAINHAND)
) {
    override fun getManager(player: Player): EnchantmentManager {
        return BreakthroughManager(player)
    }
}

// Tridents