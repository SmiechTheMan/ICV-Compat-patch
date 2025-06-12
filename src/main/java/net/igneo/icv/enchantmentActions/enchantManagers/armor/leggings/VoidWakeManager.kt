package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.entity.leggings.voidSpike.VoidSpikeEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class VoidWakeManager(player: Player?) :
    ArmorEnchantManager(EnchantType.LEGGINGS, 300, -10, true, player) {
    private var storedtick = 0
    private var removing = false
    private val spikes = ArrayList<VoidSpikeEntity>()

    override fun activate() {
        println("activating on the " + player.level())
        active = true
    }

    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun tick() {
        super.tick()
        if (active) {
            val level = player.level()
            if (activeTicks > storedtick + (if (removing) 10 else 20) && player.level() is ServerLevel) {
                if (!removing) {
                    val entity = ModEntities.VOID_SPIKE.get().create(level)
                    entity!!.setPos(player.position())
                    entity.owner = player
                    level.addFreshEntity(entity)
                    spikes.add(entity)
                    storedtick = activeTicks
                } else {
                    println("removing variable")
                    if (spikes.isNotEmpty()) {
                        spikes[spikes.size - 1].discard()
                        storedtick = activeTicks
                    } else {
                        active = false
                        resetCoolDown()
                    }
                }
            }

            if (activeTicks > 300) {
                removing = true
            }
        }
    }

    override fun dualActivate() {
        super.dualActivate()
        if (player.level() is ServerLevel) {
            val hitResult = player.pick(30.0, 0f, false)
            var position = player.position().add(player.lookAngle.scale(2.0))
            if (hitResult.type == HitResult.Type.BLOCK) {
                val blockHitResult = hitResult as BlockHitResult
                position = blockHitResult.location
            } else if (hitResult.type == HitResult.Type.ENTITY) {
                val entityHitResult = hitResult as EntityHitResult
                position = entityHitResult.entity.position()
            }
            for (entity in spikes) {
                println(hitResult.type)
                println(position)
                val pushVec = position.subtract(entity.position()).normalize().scale(3.0)
                entity.deltaMovement = Vec3(pushVec.x, position.y - entity.position().y + 0.3, pushVec.z)
                entity.launched = true
            }
        }
        active = false
        resetCoolDown()
    }

    override fun canUse(): Boolean {
        return !active
    }

    override fun resetCoolDown() {
        super.resetCoolDown()
        spikes.clear()
        storedtick = 0
        activeTicks = 0
        removing = false
    }
}


