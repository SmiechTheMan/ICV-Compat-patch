package net.igneo.icv.event

import net.igneo.icv.ICV
import net.igneo.icv.init.Keybindings
import net.igneo.icv.particle.ModParticles
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.SpriteSet
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType

@EventBusSubscriber(modid = ICV.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ModEventBusClientEvents {
    @SubscribeEvent
    fun onKeyRegister(event: RegisterKeyMappingsEvent) {
        event.register(Keybindings.helmet)
        event.register(Keybindings.chestplate)
        event.register(Keybindings.leggings)
        event.register(Keybindings.boots)
    }

    @SubscribeEvent
    fun registerParticleFactories(event: RegisterParticleProvidersEvent?) {
        Minecraft.getInstance().particleEngine.register(ModParticles.BLINK_PARTICLE.get() ) { sprite: SpriteSet? -> LodestoneWorldParticleType.Factory(sprite) }
        Minecraft.getInstance().particleEngine.register(ModParticles.WAVE_PARTICLE.get()  ) { sprite: SpriteSet? -> LodestoneWorldParticleType.Factory(sprite) }
    }
}
