package net.igneo.icv.sound

import net.igneo.icv.ICV
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

val SOUND_EVENTS: DeferredRegister<SoundEvent> = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ICV.MOD_ID)

@JvmField
val COMET_HIT: RegistryObject<SoundEvent> = registerSoundEvents("comet_hit")
val COMET_SPAWN: RegistryObject<SoundEvent> = registerSoundEvents("comet_spawn")
val GUST: RegistryObject<SoundEvent> = registerSoundEvents("gust")

@JvmField
val PARRY: RegistryObject<SoundEvent> = registerSoundEvents("parry")


val BLINK_COOLDOWN: RegistryObject<SoundEvent> = registerSoundEvents("blink_cooldown")
val BLINK_USE: RegistryObject<SoundEvent> = registerSoundEvents("blink_use")
val BLINK_USE_WALL: RegistryObject<SoundEvent> = registerSoundEvents("blink_use_wall")


val SURF_IDLE: RegistryObject<SoundEvent> = registerSoundEvents("surf_idle")
val SURF_USE: RegistryObject<SoundEvent> = registerSoundEvents("surf_use")
val SURF_COOLDOWN: RegistryObject<SoundEvent> = registerSoundEvents("surf_cooldown")

@JvmField
val SURF_PICKUP: RegistryObject<SoundEvent> = registerSoundEvents("surf_pickup")

private fun registerSoundEvents(name: String): RegistryObject<SoundEvent> {
    return SOUND_EVENTS.register(
        name
    ) { SoundEvent.createVariableRangeEvent(ResourceLocation(ICV.MOD_ID, name)) }
}

fun registerSounds(eventbus: IEventBus?) {
    SOUND_EVENTS.register(eventbus)
}
