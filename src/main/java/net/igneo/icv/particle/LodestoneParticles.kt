package net.igneo.icv.particle

import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData
import java.awt.Color

fun blinkParticles(level: Level?, pos: Vec3) {
    WorldParticleBuilder.create(BLINK_PARTICLE)
        .setLifetime(20)
        .setColorData(ColorParticleData.create(Color(164, 29, 206, 255)).build())
        .setTransparencyData(GenericParticleData.create(1f, 1f).setEasing(Easing.SINE_IN).build())
        .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
        .setScaleData(GenericParticleData.create(0.5f, 2f, 0.1f).build())
        .setSpinData(SpinParticleData.create(0f, 0f, 1f).build())
        .spawn(level, pos.x, pos.y, pos.z)
}

fun waveParticles(level: Level?, pos: Vec3, motion: Vec3) {
    println(pos)
    val startingColor = Color(124, 234, 204, 255)
    val endingColor = Color(0, 87, 187, 255)
    WorldParticleBuilder.create(WAVE_PARTICLE)
        .setScaleData(GenericParticleData.create(0.5f, 0f).build())
        .setLifetime(20)
        .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
        .addMotion(motion.x * 4, 0.01, motion.z * 4)
        .setGravityStrength(0.04f)
        .setFrictionStrength(0.05f)
        .setNoClip(false)
        .disableCull()
        .setLightLevel(6)
        .setColorData(ColorParticleData.create(startingColor, endingColor).build())
        .spawn(level, pos.x, pos.y, pos.z)
}

fun waveParticlesBright(level: Level?, pos: Vec3, motion: Vec3) {
    println(pos)
    val startingColor = Color(124, 234, 204, 255)
    val endingColor = Color(0, 87, 187, 255)
    WorldParticleBuilder.create(WAVE_PARTICLE)
        .setScaleData(GenericParticleData.create(0.5f, 0f).build())
        .setLifetime(20)
        .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
        .addMotion(motion.x * 4, 0.01, motion.z * 4)
        .setGravityStrength(0.04f)
        .setFrictionStrength(0.05f)
        .setNoClip(false)
        .disableCull()
        .setColorData(ColorParticleData.create(startingColor, endingColor).build())
        .spawn(level, pos.x, pos.y, pos.z)
}