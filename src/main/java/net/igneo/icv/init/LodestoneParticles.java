package net.igneo.icv.init;

import net.igneo.icv.particle.ModParticles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
}
