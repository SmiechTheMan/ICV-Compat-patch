package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.Utils.collectEntitiesBox
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.entity.ICVEntity
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

class BreakthroughManager(player: Player?) :
    WeaponEnchantManager(
        EnchantType.WEAPON,
        player!!,
        ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")
    ) {
    override fun activate() {
        super.activate()
        var closestEntity: ICVEntity? = null
        var lowestDistance = 99999.0
        for (e in collectEntitiesBox(
            player.level(),
            player.position().add(player.lookAngle.scale(0.3)),
            2.0
        )) {
            if (e is ICVEntity) {
                if (player.distanceTo(e) < lowestDistance) {
                    closestEntity = e
                    lowestDistance = player.distanceTo(e).toDouble()
                }
            }
        }
        closestEntity?.discard()
    }
}
