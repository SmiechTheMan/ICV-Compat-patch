package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import net.igneo.icv.ICV
import net.igneo.icv.client.animation.EnchantAnimationPlayer
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.SurfIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.entity.boots.surfWave.SurfWaveEntity
import net.igneo.icv.init.LodestoneParticles
import net.igneo.icv.init.ParticleShapes
import net.igneo.icv.sound.ModSounds
import net.igneo.icv.sound.tickable.FollowingSound
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.player.Player

class SurfManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 450, -10, true, player), EntityTracker {
    override var child: ICVEntity? = null
    private var currentAnim: EnchantAnimationPlayer? = null
    private var riding = false

    override fun tick() {
        super.tick()
        if (child != null) {
            player.setYBodyRot(player.yRot)
            if (!child!!.isAlive) {
                child = null
            } else if (!riding) {
                player.startRiding(child)
                riding = true
            }
        }
        if (currentAnim != null && !player.isPassenger) {
            currentAnim!!.stop()
            animator!!.animation = null
            currentAnim = null
        }
    }

    override fun canUse(): Boolean {
        return child == null
    }

    override val indicator: EnchantIndicator
        get() = SurfIndicator(this)

    override fun resetCoolDown() {
        super.resetCoolDown()
        riding = false
    }

    override fun onOffCoolDown(player: Player?) {
        val level = player!!.level()
        if (player.level() is ServerLevel) {
            level.playSound(
                null, player.position().x, player.position().y, player.position().z,
                ModSounds.SURF_COOLDOWN.get(),
                SoundSource.PLAYERS, 0.5f, 1.0f
            )
        }
    }

    override fun shouldTickCooldown(): Boolean {
        return child == null
    }

    override fun activate() {
        player.forcedPose = Pose.STANDING
        if (player.level() is ServerLevel) {
            child = ModEntities.SURF_WAVE.get().create(player.level())
            child!!.owner = player
            child!!.setPos(player.eyePosition)
            player.level().addFreshEntity(child)
            syncClientChild(player as ServerPlayer, child, this)
            (player as ServerPlayer).level().playSound(
                null, (player as ServerPlayer).position().x, (player as ServerPlayer).position().y, (player as ServerPlayer).position().z,
                ModSounds.SURF_USE.get(),
                SoundSource.PLAYERS, 0.5f, 1.0f
            )
            Minecraft.getInstance().soundManager.play(FollowingSound(ModSounds.SURF_IDLE.get(), child as SurfWaveEntity))
            syncClientChild(player as ServerPlayer, child, this)
        } else if (player is LocalPlayer) {
            for (pos in ParticleShapes.renderSphereList((player as LocalPlayer).level(), (player as LocalPlayer).eyePosition, 10, 10, 1f)) {
                LodestoneParticles.waveParticlesBright((player as LocalPlayer).level(), pos, pos.subtract((player as LocalPlayer).position()).scale(2.0))
            }
            currentAnim =
                EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(ResourceLocation(ICV.MOD_ID, "surfanim"))!!)
            animator!!.animation = currentAnim
        }
    }

    override fun onRemove() {
        if (this.child != null) child!!.discard()
    }
}
