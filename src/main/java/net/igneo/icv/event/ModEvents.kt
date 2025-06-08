package net.igneo.icv.event

import com.alrex.parcool.common.action.impl.Dodge
import com.alrex.parcool.common.capability.Parkourability
import net.igneo.icv.ICV
import net.igneo.icv.enchantmentActions.Input.Companion.getInput
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.StasisManager
import net.igneo.icv.init.ICVUtils.sendCooldownDamageBonuses
import net.igneo.icv.init.ICVUtils.updateManager
import net.igneo.icv.init.ICVUtils.useEnchant
import net.igneo.icv.init.Keybindings
import net.igneo.icv.networking.ModMessages
import net.igneo.icv.networking.packet.EnchantHitS2CPacket
import net.igneo.icv.networking.packet.EnchantUseC2SPacket
import net.igneo.icv.networking.packet.EquipmentUpdateS2CPacket
import net.igneo.icv.networking.packet.InputSyncC2SPacket
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
import net.minecraftforge.common.util.NonNullConsumer
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = ICV.MOD_ID)
object ModEvents {
    private var storedInput = 0

    @SubscribeEvent
    fun equipEvent(event: LivingEquipmentChangeEvent) {
        val player = event.entity as Player
        if (event.entity is Player) {
            ModMessages.sendToPlayer(
                EquipmentUpdateS2CPacket(event.slot.filterFlag),
                player as ServerPlayer
            )
            updateManager(player, event.slot.filterFlag)
        }
    }

    @SubscribeEvent
    fun onItemUseEvent(event: RightClickItem) {
        event.entity.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                if (event.entity.isCrouching) {
                    if (enchVar.getManager(5) != null) {
                        useEnchant(event.entity, 5)
                    } else {
                        useEnchant(event.entity, 4)
                    }
                } else {
                    useEnchant(event.entity, 4)
                }
            }
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    fun onKeyInputEvent(event: InputEvent.Key?) {
        if (Keybindings.boots.isDown) {
            useEnchant(Minecraft.getInstance().player!!, 0)
            ModMessages.sendToServer(EnchantUseC2SPacket(0))
        }
        if (Keybindings.leggings.isDown) {
            useEnchant(Minecraft.getInstance().player!!, 1)
            ModMessages.sendToServer(EnchantUseC2SPacket(1))
        }
        if (Keybindings.chestplate.isDown) {
            useEnchant(Minecraft.getInstance().player!!, 2)
            ModMessages.sendToServer(EnchantUseC2SPacket(2))
        }
        if (Keybindings.helmet.isDown) {
            useEnchant(Minecraft.getInstance().player!!, 3)
            ModMessages.sendToServer(EnchantUseC2SPacket(3))
        }

        var keyID = 0
        if (Minecraft.getInstance().options.keyDown.isDown) keyID = 4
        if (Minecraft.getInstance().options.keyLeft.isDown) {
            keyID = if (Minecraft.getInstance().options.keyUp.isDown) {
                7
            } else if (Minecraft.getInstance().options.keyDown.isDown) {
                5
            } else {
                6
            }
        }
        if (Minecraft.getInstance().options.keyRight.isDown) {
            keyID = if (Minecraft.getInstance().options.keyUp.isDown) {
                1
            } else if (Minecraft.getInstance().options.keyDown.isDown) {
                3
            } else {
                2
            }
        }
        if (keyID != storedInput) {
            storedInput = keyID
            ModMessages.sendToServer(InputSyncC2SPacket(keyID))
            val finalKeyID = keyID
            Minecraft.getInstance().player!!.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent { enchVar: PlayerEnchantmentActions ->
                    enchVar.input = getInput(finalKeyID)
                }
        }
    }

    /*
    @SubscribeEvent
    fun blockBreakEvent(event: BreakEvent) {
        if (EnchantmentHelper.getEnchantments(event.player.mainHandItem).containsKey(BRUTE_TOUCH.get())) {
            event.level.setBlock(event.pos, Blocks.AIR.defaultBlockState(), 2)
            event.player.mainHandItem.damageValue = event.player.mainHandItem.damageValue + 1
            event.isCanceled = true
        }
    }
    */

    @SubscribeEvent
    fun hurtEvent(event: AttackEntityEvent) {
        if (event.target !is LivingEntity) {
            event.entity.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent { enchVar: PlayerEnchantmentActions ->
                    val manager = enchVar.getManager(0) as StasisManager
                    if (enchVar.getManager(0) is StasisManager) {
                        if (manager.entityData.containsKey(event.target)) {
                            manager.addMovement(event.target, event.entity.lookAngle.normalize().scale(0.2))
                        }
                    }
                }
        }
    }

    @SubscribeEvent
    fun livingHurtEvent(event: LivingHurtEvent) {
        val player = event.source.entity as Player
        if (event.source.entity is Player) {
            sendCooldownDamageBonuses(player)
            ModMessages.sendToPlayer(EnchantHitS2CPacket(), player as ServerPlayer)
        }
        if (event.entity is ServerPlayer) {
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                .ifPresent(
                    NonNullConsumer {
                        if (Parkourability.get(player)!!
                                .get(
                                    Dodge::class.java
                                ).isDoing
                        ) {
                            event.isCanceled = true
                        }
                    })
        }
    }

    @SubscribeEvent
    fun onAttachCapabilitiesPlayer(event: AttachCapabilitiesEvent<Entity>) {
        if (event.getObject() is Player) {
            if (!event.getObject()
                    .getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).isPresent
            ) {
                event.addCapability(ResourceLocation(ICV.MOD_ID, "properties"), PlayerEnchantmentActionsProvider())
            }
        }
    }

    @SubscribeEvent
    fun onPlayerCloned(event: PlayerEvent.Clone) {
        event.original.reviveCaps()
        event.original.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { oldStore: PlayerEnchantmentActions? ->
                event.entity.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
                    .ifPresent { newStore: PlayerEnchantmentActions ->
                        newStore.copyFrom(oldStore)
                    }
            }
        event.original.invalidateCaps()
    }

    @SubscribeEvent
    fun onPlayerChangeDimension(event: PlayerChangedDimensionEvent) {
        event.entity.reviveCaps()
        event.entity.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { }
    }

    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {
        event.register(PlayerEnchantmentActions::class.java)
    }

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        event.player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS)
            .ifPresent { enchVar: PlayerEnchantmentActions ->
                for (manager in enchVar.managers) {
                    manager?.tick()
                }
            }
    }
}
