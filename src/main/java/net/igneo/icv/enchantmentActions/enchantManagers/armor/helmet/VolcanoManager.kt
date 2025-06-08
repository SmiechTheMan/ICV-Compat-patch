package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.init.ICVUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class VolcanoManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 300, -10, true, player) {
    private val selectedLocations: MutableList<BlockPos> = ArrayList()
    override fun activate() {
        if (selectedLocations.size < MAX_LOCATIONS) {
            resetCoolDown()
            val hitResult = player!!.pick(20.0, 0f, false)

            if (hitResult.type == HitResult.Type.BLOCK) {
                val blockHitResult = hitResult as BlockHitResult
                val targetPos = blockHitResult.blockPos

                selectedLocations.add(targetPos)
            }
        }
        if (selectedLocations.size != MAX_LOCATIONS) {
            return
        }
        active = true
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun canUse(): Boolean {
        return !active
    }

    override fun dualActivate() {
        resetCoolDown()
        val level = player!!.level() as ServerLevel
        if (player!!.level() !is ServerLevel) {
            return
        }
        if (selectedLocations.size == MAX_LOCATIONS) {
            triggerVolcanoEffect(level)
        }
        selectedLocations.clear()
        active = false
        resetCoolDown()
    }

    override var isDualUse: Boolean
        get() = true
        set(isDualUse) {
            super.isDualUse = isDualUse
        }

    private fun triggerVolcanoEffect(level: ServerLevel) {
        for (location in selectedLocations) {
            level.sendParticles(
                ParticleTypes.EXPLOSION, player!!.x, player!!.y, player!!.z,
                5, 0.3, 0.3, 0.3, 0.05
            )

            val entities = ICVUtils.collectEntitiesBox(player!!.level(), location.center, VOLCANO_RANGE)

            for (entity in entities) {
                if (entity === player) {
                    return
                }
                entity.hurt(level.damageSources().inFire(), VOLCANO_DAMAGE.toFloat())
            }
        }
    }

    private fun getSelectedLocations(): List<BlockPos> {
        return ArrayList(selectedLocations)
    }

    companion object {
        private const val MAX_LOCATIONS = 3
        private const val VOLCANO_DAMAGE = 10L // NEEDS TO BE CHANGED TOO MUCH DAMAGE
        private const val VOLCANO_RANGE = 5.0
    }
}