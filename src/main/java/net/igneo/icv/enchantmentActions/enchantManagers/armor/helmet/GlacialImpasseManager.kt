package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.EntityTracker
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpike.IceSpikeEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

class GlacialImpasseManager(player: Player?) :
    ArmorEnchantManager(EnchantType.HELMET, 300, -10, false, player), EntityTracker {
    override var child: ICVEntity? = null
    private var iceSpikeSpawner: ICVEntity? = null
    private var lifetime = 0

    override fun activate() {
        lifetime = 0
        iceSpikeSpawner = ModEntities.ICE_SPIKE_SPAWNER.get().create(player.level())
        if (player.level() !is ServerLevel) {
            return
        }
        iceSpikeSpawner!!.owner = player
        iceSpikeSpawner!!.setPos(player.eyePosition.add(player.getViewVector(1.0f).normalize()))
        iceSpikeSpawner?.let { player.level().addFreshEntity(it) }
        syncClientChild(player as ServerPlayer, iceSpikeSpawner, this)

        active = true
    }

    override fun tick() {
        super.tick()
        val level = player.level()
        if (iceSpikeSpawner == null || player.level() !is ServerLevel) {
            return
        }

        val checkRadius = 1.3
        val tooClose: Boolean = level.getEntities<IceSpikeEntity>(ModEntities.ICE_SPIKE.get(),
            iceSpikeSpawner!!.boundingBox.inflate(checkRadius)
        ) { spike: IceSpikeEntity? ->
            spike != null &&
                    spike.distanceTo(iceSpikeSpawner) < checkRadius
        }.isNotEmpty()

        if (tooClose) {
            return
        }

        child = ModEntities.ICE_SPIKE.get().create(player.level())
        if (child == null) {
            return
        }

        (child as IceSpikeEntity).setPos(iceSpikeSpawner!!.position().add(iceSpikeSpawner!!.position().normalize().scale(-1.0)))
        player.level().addFreshEntity(child)
        syncClientChild(player as ServerPlayer, child, this)

        if (lifetime < 200) {
            lifetime++
        } else {
            iceSpikeSpawner = null
            child = null
        }
    }


    override fun onOffCoolDown(player: Player?) {
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)
}