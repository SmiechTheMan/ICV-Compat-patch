package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import java.util.*

class KineticManager(player: Player?) :
    WeaponEnchantManager(EnchantType.WEAPON, player, ResourceLocation(ICV.MOD_ID, "kinetic")) {
    private var kineticChargeCount = 0
    private var kineticTimer = 0
    private var damageBoostActive = false

    override fun onAttack(entity: Entity?) {
        kineticChargeCount++
        kineticTimer = 0

        val speedBonus = 0.25 * kineticChargeCount
        player!!.attributes.getInstance(Attributes.MOVEMENT_SPEED)
            ?.addTransientModifier(
                AttributeModifier(
                    SPEED_MODIFIER_UUID,
                    "Kinetic Speed Boost",
                    speedBonus,
                    AttributeModifier.Operation.ADDITION
                )
            )

        if (damageBoostActive) {
            damageBoostActive = false
        }
    }

    override fun activate() {
        if (kineticChargeCount > 0) {
            val bonusDamage = kineticChargeCount * 1.5
            player!!.attributes.getInstance(Attributes.MOVEMENT_SPEED)!!.removeModifier(
                SPEED_MODIFIER_UUID
            )
            player!!.attributes
                .getInstance(Attributes.ATTACK_DAMAGE)!!
                .addTransientModifier(
                    AttributeModifier(
                        DAMAGE_MODIFIER_UUID,
                        "Kinetic Damage Boost",
                        bonusDamage,
                        AttributeModifier.Operation.ADDITION
                    )
                )

            kineticChargeCount = 0
            damageBoostActive = true
        }
    }

    override fun tick() {
        if (kineticChargeCount < 0) {
            return
        }
        kineticTimer++
        if (kineticTimer <= 180) {
            return
        }
        resetKineticEffects()
    }

    private fun resetKineticEffects() {
        kineticChargeCount = 0
        kineticTimer = 0
        damageBoostActive = false
        player!!.attributes.getInstance(Attributes.MOVEMENT_SPEED)!!.removeModifier(
            SPEED_MODIFIER_UUID
        )
        player!!.attributes.getInstance(Attributes.ATTACK_DAMAGE)!!.removeModifier(
            DAMAGE_MODIFIER_UUID
        )
    }

    override fun canUse(): Boolean {
        return true
    }

    companion object {
        private val SPEED_MODIFIER_UUID: UUID = UUID.fromString("f72bbef0-6198-4994-96f4-b975a86e2085")
        private val DAMAGE_MODIFIER_UUID: UUID = UUID.fromString("a8b4e7d1-9c65-4e73-8f12-d09a8c621b37")
    }
}
