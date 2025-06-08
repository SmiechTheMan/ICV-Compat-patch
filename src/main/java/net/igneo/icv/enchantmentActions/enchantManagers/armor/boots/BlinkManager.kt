package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots

import net.igneo.icv.client.indicators.BlinkCooldownIndicator
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils.sendPacketInRange
import net.igneo.icv.init.LodestoneParticles
import net.igneo.icv.init.ParticleShapes
import net.igneo.icv.networking.packet.SendBlinkShaderS2CPacket
import net.igneo.icv.sound.ModSounds
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class BlinkManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 300, -30, false, player) {
    override val indicator: EnchantIndicator
        get() = BlinkCooldownIndicator(this)

    override fun onOffCoolDown(player: Player?) {
        checkNotNull(player)
        val level = player.level() as ServerLevel
        if (player.level() is ServerLevel) {
            level.playSound(
                null, player.position().x, player.position().y, player.position().z,
                ModSounds.BLINK_COOLDOWN.get(),
                SoundSource.PLAYERS, 0.5f, 1.0f
            )
        }
    }

    override fun canUse(): Boolean {
        return true
    }

    override fun activate() {
        val playerPos = player!!.eyePosition
        val lookVector = player!!.lookAngle.normalize()
        var targetPos = playerPos.add(
            lookVector.x * BLINK_DISTANCE,
            (lookVector.y * BLINK_DISTANCE) - 1,
            lookVector.z * BLINK_DISTANCE
        )

        val result = player!!.level().clip(
            ClipContext(
                playerPos,
                targetPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
            )
        )

        // Fixed: Use HitResult.Type instead of BlockHitResult.Type
        if (result.type != HitResult.Type.MISS) {
            val hitPos = result.location
            targetPos = hitPos.subtract(
                lookVector.x * 0.5f,
                lookVector.y * 2.5f,
                lookVector.z * 0.5f
            )
        }

        player!!.teleportTo(
            targetPos.x,
            targetPos.y,
            targetPos.z
        )
        val level = player!!.level() as ServerLevel
        if (player!!.level() is ServerLevel) {
            ParticleShapes.renderLine(level, playerPos, targetPos, ParticleTypes.PORTAL, 10)

            LodestoneParticles.blinkParticles(level, targetPos.add(player!!.lookAngle.scale(8.0)))
            level.playSound(
                null, targetPos.x, targetPos.y, targetPos.z,
                ModSounds.BLINK_USE_WALL.get(),
                SoundSource.PLAYERS, 0.5f, 1.0f
            )
            level.playSound(
                null, playerPos.x, playerPos.y, playerPos.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 0.5f, 1.0f
            )

            targetPos = Vec3(targetPos.x, targetPos.y - 0.5, targetPos.z)
            sendPacketInRange<SendBlinkShaderS2CPacket>(level, targetPos, 200f, SendBlinkShaderS2CPacket(targetPos))
        }
        LodestoneParticles.blinkParticles(player!!.level(), playerPos)

        //for (Vec3 pos : ParticleShapes.renderRingList(player.level(),targetPos,10,1.5F)) {
        //  System.out.println(pos);
        //  LodestoneParticles.blinkParticles(player.level(), pos);
        //}
    }

    companion object {
        private const val BLINK_DISTANCE = 10.0
    }
}