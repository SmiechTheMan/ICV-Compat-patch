package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.particle.ModParticlesKt;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import static net.igneo.icv.init.KeybindingsKt.*;

@Mod.EventBusSubscriber (modid = ICV.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(helmet);
        event.register(chestplate);
        event.register(leggings);
        event.register(boots);
    }
    
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticlesKt.getBLINK_PARTICLE().get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(ModParticlesKt.getWAVE_PARTICLE().get(), LodestoneWorldParticleType.Factory::new);
    }
}
