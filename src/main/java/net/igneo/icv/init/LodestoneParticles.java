package net.igneo.icv.init;

import net.igneo.icv.particle.ModParticles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

public class LodestoneParticles {
    public static void blinkParticles(Level level, Vec3 pos) {
        WorldParticleBuilder.create(ModParticles.BLINK_PARTICLE)
                .setLifetime(20)
                .setColorData(ColorParticleData.create(new Color(164, 29, 206, 255)).build())
                .setTransparencyData(GenericParticleData.create(1f, 1f).setEasing(Easing.SINE_IN).build())
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .setScaleData(GenericParticleData.create(0.5F, 2, 0.1F).build())
                .setSpinData(SpinParticleData.create(0, 0, 1).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }
    public static void waveParticles(Level level, Vec3 pos, Vec3 motion) {
        System.out.println(pos);
        Color startingColor = new Color(124, 234, 204,255);
        Color endingColor = new Color(0, 87, 187,255);
        WorldParticleBuilder.create(ModParticles.WAVE_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setLifetime(20)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .addMotion(motion.x*4, 0.01f, motion.z*4)
                .setGravityStrength(0.04f)
                .setFrictionStrength(0.05F)
                .setNoClip(false)
                .disableCull()
                .setLightLevel(6)
                .setColorData(ColorParticleData.create(startingColor, endingColor).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }
    public static void waveParticlesBright(Level level, Vec3 pos, Vec3 motion) {
        System.out.println(pos);
        Color startingColor = new Color(124, 234, 204,255);
        Color endingColor = new Color(0, 87, 187,255);
        WorldParticleBuilder.create(ModParticles.WAVE_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setLifetime(20)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .addMotion(motion.x*4, 0.01f, motion.z*4)
                .setGravityStrength(0.04f)
                .setFrictionStrength(0.05F)
                .setNoClip(false)
                .disableCull()
                .setColorData(ColorParticleData.create(startingColor, endingColor).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }
}
