package net.igneo.icv.sound.tickable

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity

class FollowingSound(sound: SoundEvent, var entity: Entity) :
    AbstractTickableSoundInstance(sound, SoundSource.PLAYERS, RandomSource.create()) {
    init {
        this.delay = 0
        this.x = entity.x
        this.y = entity.y
        this.z = entity.z
    }

    override fun tick() {
        this.x = entity.x
        this.y = entity.y
        this.z = entity.z
    }
}
