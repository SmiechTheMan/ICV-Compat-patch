package net.igneo.icv.enchantmentActions.enchantManagers.weapon

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.networking.packet.PushPlayerS2CPacket
import net.igneo.icv.networking.sendToPlayer
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class TungstenCoreManager(player: Player?) :
    WeaponEnchantManager(
        EnchantType.WEAPON,
        player!!,
        ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross")
    ) {
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
        player.attributes.getInstance(Attributes.ATTACK_KNOCKBACK)!!.addTransientModifier(
            AttributeModifier(
                KNOCKBACK_MODIFIER_UUID,
                "Tungsten Core Knockback boost",
                2.0,
                AttributeModifier.Operation.ADDITION
            )
        )
    }

    override fun removePassive() {
        super.removePassive()
        player.attributes.getInstance(Attributes.ATTACK_KNOCKBACK)!!.removeModifier(
            KNOCKBACK_MODIFIER_UUID
        )
    }

    override fun activate() {
        super.activate()

        val level = target!!.level()
        if (player.level() is ServerLevel) {
            val radius = 3.0f
            val pushStrength = 3.5f

            renderRadius(level as ServerLevel, player.position(), radius)

            val nearbyEntities: List<Entity> = level.getEntities(
                player,
                player.boundingBox.inflate(radius.toDouble())
            ) { entity: Entity -> entity !== player }

            for (entity in nearbyEntities) {
                val direction = entity.position().subtract(player.position()).normalize()

                val pushVelocity = direction.scale(pushStrength.toDouble())
                entity.addDeltaMovement(pushVelocity)
                entity.hurtMarked = true

                if (entity is ServerPlayer) {
                    sendToPlayer(PushPlayerS2CPacket(pushVelocity), entity)
                }
            }
        }
    }

    private fun renderRadius(level: ServerLevel, center: Vec3, radius: Float) {
        val hitboxParticle: ParticleOptions = ParticleTypes.ELECTRIC_SPARK

        val pointsPerRing = 32
        val yOffset = 0.05f

        for (i in 0 until pointsPerRing) {
            val angle = (2 * Math.PI * i / pointsPerRing).toFloat()
            val x = (center.x + radius * cos(angle.toDouble())).toFloat()
            val z = (center.z + radius * sin(angle.toDouble())).toFloat()

            level.sendParticles(
                hitboxParticle,
                x.toDouble(), center.y + yOffset, z.toDouble(),
                1, 0.0, 0.0, 0.0, 0.0
            )
        }
    }

    companion object {
        val KNOCKBACK_MODIFIER_UUID: UUID = UUID.fromString("f53860da-d668-41db-bad4-e9fb3fbefe41")
    }
}