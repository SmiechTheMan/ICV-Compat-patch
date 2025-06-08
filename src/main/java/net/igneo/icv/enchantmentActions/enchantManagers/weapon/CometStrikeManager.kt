package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.sound.ModSounds
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import java.util.*

class CometStrikeManager(player: Player?) :
    WeaponEnchantManager(EnchantType.WEAPON, player, ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")) {
    private var cometSpawn: BlockPos? = null
    override fun onEquip() {
        super.onEquip()
        applyPassive()
    }

    override fun onRemove() {
        super.onRemove()
        removePassive()
    }

    override fun applyPassive() {
        super.applyPassive()
        println("applying buff")
        player!!.attributes.getInstance(Attributes.MOVEMENT_SPEED)!!.addTransientModifier(
            AttributeModifier(
                COMET_SPEED_MODIFIER_UUID,
                "Comet strike speed boost",
                0.02,
                AttributeModifier.Operation.ADDITION
            )
        )
    }

    override fun removePassive() {
        player!!.attributes.getInstance(Attributes.MOVEMENT_SPEED)!!.removeModifier(
            COMET_SPEED_MODIFIER_UUID
        )
    }

    private fun spawnComet() {
        val level = target!!.level()
        if (player!!.level() is ServerLevel) {
            if (cometSpawn == null) {
                println("grabbing new location")
                var x = 0
                var z = 0
                val i = 0.4
                if (player!!.lookAngle.x > i) {
                    x = 2
                } else if (player!!.lookAngle.x < -i) {
                    x = -2
                }
                if (player!!.lookAngle.z > i) {
                    z = 2
                } else if (player!!.lookAngle.z < -i) {
                    z = -2
                }
                cometSpawn = BlockPos(player!!.blockX + x, player!!.blockY, player!!.blockZ + z)
            }
            if (activeTicks > 30) {
                ModEntities.COMET.get().spawn(level as ServerLevel, cometSpawn, MobSpawnType.COMMAND)
                level.playSound(null, cometSpawn, ModSounds.COMET_SPAWN.get(), SoundSource.PLAYERS, 0.5f, 1f)
                active = false
                cometSpawn = null
            }
        }
    }

    override fun tick() {
        super.tick()
        if (activeTicks > 10) {
            spawnComet()
        }
    }

    companion object {
        val COMET_SPEED_MODIFIER_UUID: UUID = UUID.fromString("8a23719c-852d-47fc-bb41-8527955288d4")
    }
}
