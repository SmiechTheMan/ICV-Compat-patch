package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import com.alrex.parcool.common.action.impl.Dodge
import com.alrex.parcool.common.action.impl.Slide
import com.alrex.parcool.common.capability.Parkourability
import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.init.ICVUtils
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

class VolatileManager(player: Player?) :
    WeaponEnchantManager(EnchantType.WEAPON, player, ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")) {
    override fun onAttack(entity: Entity?) {
        if (player!!.fallDistance > 0) {
            for (entity in target?.let { ICVUtils.collectEntitiesBox(it.level(), target!!.position(), 3.0) }!!) {
                if (entity !== player && entity !== target) {
                    entity.hurt(player!!.damageSources().explosion(player, entity), 7f)
                }
            }
        }
    }

    override fun activate() {
        super.activate()
        if (player?.level() is ServerLevel) {
            val child = ModEntities.BOOST_CHARGE.get().create(player!!.level())
            child!!.owner = player
            child.setPos(player!!.eyePosition.subtract(0.0, 1.0, 0.0))
            child.deltaMovement = player!!.lookAngle.scale(0.6)
            player?.level()?.addFreshEntity(child)
        }
    }

    override fun stableCheck(): Boolean {
        return !enchVar!!.animated &&
                !player?.isSwimming!! &&
                !Parkourability.get(player)!!.get(Dodge::class.java).isDoing &&
                !Parkourability.get(player)!!.get(Slide::class.java).isDoing
    }
}
