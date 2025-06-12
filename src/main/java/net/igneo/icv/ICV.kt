package net.igneo.icv

import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory
import net.igneo.icv.client.HUD_ENCHANTMENTS
import net.igneo.icv.config.ICVClientConfigs
import net.igneo.icv.config.ICVCommonConfigs
import net.igneo.icv.enchantment.registerEnchantments
import net.igneo.icv.entity.registerEntities
import net.igneo.icv.entity.registerEntityRenderer
import net.igneo.icv.networking.registerPackets
import net.igneo.icv.particle.registerParticles
import net.igneo.icv.shader.registerShaders
import net.igneo.icv.sound.registerSounds
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(ICV.MOD_ID)
class ICV {

    init {
        val modEventBus = FMLJavaModLoadingContext.get().modEventBus
        modEventBus.addListener(this::onCommonSetup)
        MinecraftForge.EVENT_BUS.register(this)

        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.CLIENT, ICVClientConfigs.spec, "icv-client.toml")
            registerConfig(ModConfig.Type.COMMON, ICVCommonConfigs.spec, "icv-common.toml")
        }

        registerEnchantments(modEventBus)
        registerParticles(modEventBus)
        registerSounds(modEventBus)
        registerEntities(modEventBus)
    }

    private fun onCommonSetup(event: FMLCommonSetupEvent) {
        registerPackets()
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
    object ClientModEvents {

        @SubscribeEvent
        fun onClientSetup(event: FMLClientSetupEvent) {
            registerShaders()
            registerEntityRenderer()

            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ResourceLocation(MOD_ID, "enchant_animator"),
                42
            ) {
                registerPlayerAnimation()
            }
        }

        @SubscribeEvent
        fun onRegisterGuiOverlays(event: RegisterGuiOverlaysEvent) {
            event.registerAboveAll("enchantments", HUD_ENCHANTMENTS)
        }
    }

    companion object {
        const val MOD_ID = "icv"
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)

        fun registerPlayerAnimation(): IAnimation {
            return ModifierLayer<IAnimation>()
        }
    }
}

