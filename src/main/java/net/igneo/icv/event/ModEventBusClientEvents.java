package net.igneo.icv.event;

import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import net.igneo.icv.ICV;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.helmet);
        event.register(Keybindings.chestplate);
        event.register(Keybindings.leggings);
        event.register(Keybindings.boots);
    }
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.BLINK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
    }
}
