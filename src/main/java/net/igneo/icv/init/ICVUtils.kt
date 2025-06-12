package net.igneo.icv.init

import net.igneo.icv.enchantment.ICVEnchantment
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.Input
import net.igneo.icv.enchantmentActions.Input.Companion.getRotation
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.networking.ModMessages
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.cos
import kotlin.math.sin

object ICVUtils {
    @JvmStatic
    fun getFlatInputDirection(rot: Float, input: Input, scale: Float, yVelocity: Double): Vec3 {
        val rotation = getRotation(input)
        val yaw = Math.toRadians((rot + rotation).toDouble())
        val x = -sin(yaw)
        val z = cos(yaw)

        return Vec3(x * scale, yVelocity, z * scale)
    }

    @JvmStatic
    fun getFlatDirection(rot: Float, scale: Float, yVelocity: Double): Vec3 {
        val yaw = Math.toRadians(rot.toDouble())
        val x = -sin(yaw)
        val z = cos(yaw)

        return Vec3(x * scale, yVelocity, z * scale)
    }

    object GetManagerForType{
        @JvmStatic
        fun <T : EnchantmentManager?> getManagerForType(player: Player, desiredManager: Class<T>): EnchantmentManager? {
            val returnManager = AtomicReference<T?>(null)
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent { enchVar: PlayerEnchantmentActions ->
                    for (manager in enchVar.managers) {
                        println("testing manager: $manager for type: $desiredManager")
                        if (manager != null && manager.javaClass == desiredManager) {
                            println("returning the manager")
                            returnManager.set(manager as T)
                        }
                    }
                }
            return returnManager.get()
        }
    }


    fun <T : EnchantmentManager?> getSlotForType(player: Player, desiredManager: Class<T>): Int {
        val slot = AtomicInteger(-1)
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                for ((tempSlot, manager) in enchVar.managers.withIndex()) {
                    if (manager != null && manager.javaClass == desiredManager) {
                        slot.set(tempSlot)
                    }
                }
            }
        return slot.get()
    }

    @JvmStatic
    fun collectEntitiesBox(level: Level, position: Vec3, radius: Double): List<Entity> {
        val scale = Vec3(radius, radius, radius)
        return level.getEntities(null, AABB(position.subtract(scale), position.add(scale)))
    }

    @JvmStatic
    @OnlyIn(Dist.CLIENT)
    fun directUpdate(pSlot: Int) {
        if (Minecraft.getInstance().player != null) {
            updateManager(Minecraft.getInstance().player!!, pSlot)
        }
    }

    @JvmStatic
    fun updateManager(player: Player, pSlot: Int) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                var slot = pSlot
                if (pSlot == 0) {
                    slot = 4
                } else if (pSlot <= 4) {
                    --slot
                }
                var enchList: List<Enchantment> = ArrayList()
                when (slot) {
                    0, 1, 2, 3 -> enchList = player.inventory.getArmor(slot).allEnchantments.keys.stream().toList()
                    4 -> enchList = player.mainHandItem.allEnchantments.keys.stream().toList()
                    5 -> enchList = player.offhandItem.allEnchantments.keys.stream().toList()
                }
                if (enchList.isNotEmpty()) {
                    for (enchantment in enchList) {
                        if (enchantment is ICVEnchantment) {
                            enchVar.setManager(enchantment.getManager(player), slot)
                        }
                    }
                } else {
                    enchVar.setManager(null, slot)
                }
            }
    }

    @JvmStatic
    fun useEnchant(player: Player, slot: Int) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                if (enchVar.getManager(slot) != null) {
                    enchVar.getManager(slot)!!.use()
                }
            }
    }

    @JvmStatic
    @OnlyIn(Dist.CLIENT)
    fun syncClientEntity(iD: Int, slot: Int) {
        checkNotNull(Minecraft.getInstance().player)
        Minecraft.getInstance().player!!.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                val manager = enchVar.getManager(slot) as EntityTracker
                if (enchVar.getManager(slot) is EntityTracker) {
                    checkNotNull(Minecraft.getInstance().level)
                    println(Minecraft.getInstance().level!!.getEntity(iD))
                    println(iD)
                    if (iD == -1) {
                        manager.child = null
                    } else if (Minecraft.getInstance().level!!.getEntity(iD) is ICVEntity) {
                        val entity = Minecraft.getInstance().level!!.getEntity(iD) as ICVEntity
                        println("synced!")
                        entity.owner = Minecraft.getInstance().player
                        manager.child = entity
                    }
                }
            }
    }

    @JvmStatic
    @OnlyIn(Dist.CLIENT)
    fun clientCooldownDamageBonuses() {
        checkNotNull(Minecraft.getInstance().player)
        sendCooldownDamageBonuses(Minecraft.getInstance().player!!)
    }

    @JvmStatic
    fun sendCooldownDamageBonuses(player: Player) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                for (manager in enchVar.managers) {
                    if (manager is ArmorEnchantManager) {
                        manager.targetDamaged()
                    }
                }
            }
    }

    fun getItemForSlot(player: Player, slot: Int): ItemStack {
        return when (slot) {
            (1) -> player.inventory.getArmor(1)
            (2) -> player.inventory.getArmor(2)
            (3) -> player.inventory.getArmor(3)
            (4) -> player.mainHandItem
            (5) -> player.offhandItem
            else -> player.inventory.getArmor(0)
        }
    }

    val DOWN_VECTOR: Vec3 = Vec3(0.0, -1.0, 0.0)
    @JvmField
    val UP_VECTOR: Vec3 = Vec3(0.0, 1.0, 0.0)
    val FOWARD_VECTOR: Vec3 = Vec3(0.0, 0.0, 1.0)
    val BACKWORD_VECTOR: Vec3 = Vec3(0.0, 0.0, -1.0)
    val LEFT_VECTOR: Vec3 = Vec3(-1.0, 0.0, 0.0)
    val RIGHT_VECTOR: Vec3 = Vec3(1.0, 0.0, 0.0)

    @JvmStatic
    fun getDamageFromItem(item: ItemStack, entity: Entity): Double {
        val modifiers = item.getAttributeModifiers(EquipmentSlot.MAINHAND)
        val damageModifiers = modifiers[Attributes.ATTACK_DAMAGE]
        var baseDamage = 0.0
        for (mod in damageModifiers) {
            baseDamage += mod.amount
        }
        if (entity is Player) baseDamage += entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE)

        return baseDamage
    }

    fun <MSG> sendPacketInRange(level: ServerLevel, pos: Vec3, range: Float, message: MSG) {
        for (player in level.players()) {
            if (player.distanceToSqr(pos) < range) ModMessages.sendToPlayer(message, player)
        }
    }
}
