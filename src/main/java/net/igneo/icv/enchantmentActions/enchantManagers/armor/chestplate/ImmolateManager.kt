package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate

import net.igneo.icv.Utils.raycastGround
import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.igneo.icv.particle.renderSphere
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

private val raycastHits = mutableMapOf<Int, BlockPos>()

class ImmolateManager(player: Player?) :
    ArmorEnchantManager(EnchantType.CHESTPLATE, 300, -10, true, player) {
    private val world: Level = player!!.level()
    private val start: Vec3 = player!!.position()
    private val direction: Vec3 = player!!.lookAngle

    override fun onOffCoolDown(player: Player?) {
        TODO("Not yet implemented")
    }

    override val indicator: EnchantIndicator
        get() = StasisCooldownIndicator(this)

    override fun activate() {

        storeRaycastHit(world, start, direction, 10.0, 0)

        active = true
    }

    override fun dualActivate() {
        val keys = listOf(0, 1, 2)
        if (raycastHits.size <= 3) {
            storeRaycastHit(world, start, direction, 10.0, 1)
            storeRaycastHit(world, start, direction, 10.0, 2)
        } else {
            for (key in keys) {
                val hit = raycastHits[key]
                renderSphere(world as ServerLevel, hit!!.center, ParticleTypes.FLAME, 16, 16, 4f)
            }
        }
    }

    private fun storeRaycastHit(world: Level, start: Vec3, direction: Vec3, maxDistance: Double, key: Int) {
        val result = raycastGround(world, start, direction, maxDistance)

        if (result != null && result.type == HitResult.Type.BLOCK) {
            val hitPos = BlockPos(result.location as Vec3i)
            raycastHits[key] = hitPos
            println("Stored hit '$key' at: $hitPos")
        } else {
            println("No block hit for key: $key")
        }
    }
}