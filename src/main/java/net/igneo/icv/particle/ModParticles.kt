package net.igneo.icv.particle

import net.igneo.icv.ICV
import net.minecraft.core.particles.ParticleType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType

val PARTICLE_TYPES: DeferredRegister<ParticleType<*>> =
    DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ICV.MOD_ID)

private fun registerParticle(name: String): RegistryObject<LodestoneWorldParticleType> {
    return PARTICLE_TYPES.register(
        name
    ) { LodestoneWorldParticleType() }
}

val BLINK_PARTICLE: RegistryObject<LodestoneWorldParticleType> = PARTICLE_TYPES.register(
    "blink_particle"
) { LodestoneWorldParticleType() }
val WAVE_PARTICLE: RegistryObject<LodestoneWorldParticleType> = PARTICLE_TYPES.register(
    "wave_particle"
) { LodestoneWorldParticleType() }


fun registerParticles(eventbus: IEventBus?) {
    PARTICLE_TYPES.register(eventbus)
}
