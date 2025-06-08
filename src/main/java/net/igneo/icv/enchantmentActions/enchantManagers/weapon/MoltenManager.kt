package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.init.ICVUtils
import net.igneo.icv.sound.ModSounds
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player

class MoltenManager(player: Player?) :
    WeaponEnchantManager(EnchantType.WEAPON, player, ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")) {
    override val damageBonus: Float
        get() {
            if (target?.isOnFire == true) {
                target!!.extinguishFire()
                val level = target!!.level()
                if (level is ServerLevel) {
                    level.playSound(null, target!!.blockPosition(), ModSounds.PARRY.get(), SoundSource.PLAYERS)

                    repeat(3) {
                        val child = ModEntities.EMBER.get().create(player!!.level())
                        child?.let {
                            it.owner = player
                            it.setPos(target!!.eyePosition.subtract(0.0, 1.0, 0.0))
                            it.deltaMovement = ICVUtils.getFlatDirection(
                                (Math.random() * 360).toFloat(),
                                0.2f,
                                0.3
                            )
                            level.addFreshEntity(it)
                        }
                    }

                    return 3.0f
                }
            }

            return super.damageBonus
        }

    override fun activate() {
        super.activate()
        val child = ModEntities.FIRE_RING.get().create(player!!.level())
        child!!.owner = player
        child.setPos(player!!.position())
        //child.setDeltaMovement(ICVUtils.getFlatDirection((float) (Math.random()*360),0.2f,0.3f));
        player!!.level().addFreshEntity(child)
    }
}
