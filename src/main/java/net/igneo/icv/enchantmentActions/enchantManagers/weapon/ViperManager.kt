package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.init.ICVUtils
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class ViperManager(player: Player?) :
    WeaponEnchantManager(EnchantType.WEAPON, player, ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")),
    EntityTracker {
    override var child: ICVEntity? = null
    private var nullCheck: Boolean = false

    override fun activate() {
        super.activate()
        val level = player!!.level()
        if (player!!.level() is ServerLevel) {
            child = ModEntities.SNAKE_BITE.get().create(player!!.level())
            child!!.owner = player
            child!!.setPos(player!!.eyePosition.subtract(0.0, 1.0, 0.0))
            child!!.deltaMovement = ICVUtils.getFlatDirection(player!!.yRot, 1f, 0.0)
            level.addFreshEntity(child)
            syncClientChild(player as ServerPlayer, child, this)
            nullCheck = true
        }
    }

    override val damageBonus: Float
        get() {
            val living = target as? LivingEntity ?: return 0f

            if (living.activeEffects.isNotEmpty()) {
                for (instance in living.activeEffects) {
                    val effect = instance.effect
                    if (!effect.isBeneficial) {
                        val duration = instance.duration - 20
                        living.removeEffect(effect)
                        if (duration > 0) {
                            living.addEffect(
                                MobEffectInstance(
                                    effect,
                                    duration,
                                    instance.amplifier,
                                    instance.isAmbient,
                                    instance.isVisible
                                )
                            )
                        }
                    }
                }
                return 4f
            }

            return 0f
        }


    override fun canUse(): Boolean {
        return super.canUse() && child == null
    }

    override fun tick() {
        super.tick()
        if (nullCheck && player is ServerPlayer && child == null) {
            syncClientChild(player as ServerPlayer, null, this)
            nullCheck = false
        }
    }
}