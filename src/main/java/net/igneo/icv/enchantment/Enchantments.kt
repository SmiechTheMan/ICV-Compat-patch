package net.igneo.icv.enchantment

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.*
import net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate.*
import net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet.*
import net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings.*
import net.igneo.icv.enchantmentActions.enchantManagers.trident.*
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.*
import net.igneo.icv.networking.packet.EnchantAttackS2CPacket
import net.igneo.icv.networking.sendToPlayer
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
    ICVEnchantRarity: Rarity,
    ICVEnchantCategory: EnchantmentCategory,
    ICVEnchantApplicableSlots: Array<out EquipmentSlot>
) : Enchantment(ICVEnchantRarity, ICVEnchantCategory, ICVEnchantApplicableSlots) {
    abstract fun getManager(player: Player): EnchantmentManager
}

abstract class WeaponEnchantment(
    ICVEnchantRarity: Rarity,
    ICVEnchantCategory: EnchantmentCategory,
    ICVEnchantApplicableSlots: Array<out EquipmentSlot>
) : ICVEnchantment(ICVEnchantRarity, ICVEnchantCategory, ICVEnchantApplicableSlots) {
    var currentLevel: ServerLevel? = null

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

class BlackHoleEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
    override fun getManager(player: Player) = BlackHoleManager(player)
}

class DivineSmiteEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
    override fun getManager(player: Player) = DivineSmiteManager(player)
}

class GlacialImpasseEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
    override fun getManager(player: Player) = GlacialImpasseManager(player)
}

class GravityWellEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
    override fun getManager(player: Player) = GravityWellManager(player)
}

class RiftRipperEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
    override fun getManager(player: Player) = RiftRipperManager(player)
}

class VolcanoEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD)) {
    override fun getManager(player: Player) = VolcanoManager(player)
}

// Chestplates

class AbyssOmenEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST)) {
    override fun getManager(player: Player) = AbyssOmenManager(player)
}

class ExtinctionEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST)) {
    override fun getManager(player: Player) = ExtinctionManager(player)
}

class HauntEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST)) {
    override fun getManager(player: Player) = HauntManager(player)
}

class ImmolateEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST)) {
    override fun getManager(player: Player) = ImmolateManager(player)
}

class MilkyChrysalisEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST)) {
    override fun getManager(player: Player) = MilkyChrysalisManager(player)
}

class PlanarShiftEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST)) {
    override fun getManager(player: Player) = PlanarShiftManager(player)
}

// Leggings

class GaleEnchantment : ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS)) {
    override fun getManager(player: Player) = GaleManager(player)
}

class HurricaneEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS)) {
    override fun getManager(player: Player) = HurricaneManager(player)
}

class JudgementEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS)) {
    override fun getManager(player: Player) = JudgementManager(player)
}

class TempestEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS)) {
    override fun getManager(player: Player) = TempestManager(player)
}

class TsunamiEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS)) {
    override fun getManager(player: Player) = TsunamiManager(player)
}

class VoidWakeEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS)) {
    override fun getManager(player: Player) = VoidWakeManager(player)
}

// Boots

class BlinkEnchantment : ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, arrayOf(EquipmentSlot.FEET)) {
    override fun getManager(player: Player) = BlinkManager(player)
}

class CurbStompEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, arrayOf(EquipmentSlot.FEET)) {
    override fun getManager(player: Player) = CurbStompManager(player)
}

class SoulEmberEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, arrayOf(EquipmentSlot.FEET)) {
    override fun getManager(player: Player) = SoulEmberManager(player)
}

class StasisEnchantment : ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, arrayOf(EquipmentSlot.FEET)) {
    override fun getManager(player: Player) = StasisManager(player)
}

class StoneCallerEnchantment :
    ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, arrayOf(EquipmentSlot.FEET)) {
    override fun getManager(player: Player) = StoneCallerManager(player)
}

class SurfEnchantment : ICVEnchantment(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, arrayOf(EquipmentSlot.FEET)) {
    override fun getManager(player: Player) = SurfManager(player)
}

// Weapons

class CascadeEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = CascadeManager(player)
}

class BurstEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = BurstManager(player)
}

class TungstenCoreEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = TungstenCoreManager(player)
}

class FinesseEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = FinesseManager(player)
}

class GustEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = GustManager(player)
}

class ViperEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = ViperManager(player)
}

class CometStrikeEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = CometStrikeManager(player)
}

class KineticEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = KineticManager(player)
}

class VolatileEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = VolatileManager(player)
}

class MoltenEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = MoltenManager(player)
}

class BreakthroughEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = BreakthroughManager(player)
}

class MeathookEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = MeathookManager(player)
}

// Tridents

class GeyserEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = GeyserManager(player)
}

class WhirlpoolEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = WhirlpoolManager(player)
}

class UpwellEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = UpwellManager(player)
}

class CavitationEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = CavitationManager(player)
}

class UndertowEnchantment :
    WeaponEnchantment(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun getManager(player: Player): EnchantmentManager = UndertowManager(player)
}

// Tools

class BruteTouchEnchantment : Enchantment(
    Rarity.UNCOMMON, EnchantmentCategory.WEAPON, arrayOf(EquipmentSlot.MAINHAND)
)