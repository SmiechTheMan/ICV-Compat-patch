package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.client.indicators.StasisCooldownIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min

class StasisManager(player: Player?) :
    ArmorEnchantManager(EnchantType.BOOTS, 300, -10, true, player) {
    @JvmField
    var entityData: HashMap<Entity, StasisEntityDataManager> = HashMap()
    private val blackList: List<Entity> = ArrayList()

    override fun activate() {
        println("activating")
        for (entity in player!!.level().getEntities(null, player!!.boundingBox.inflate(20.0))) {
            if (entity !is LivingEntity && !blackList.contains(entity)) {
                entityData[entity] = StasisEntityDataManager(Vec3.ZERO, entity.position(), entity.yRot, entity.xRot)
            }
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
        release()
        resetCoolDown()
        println("no more active")
    }

    override var isDualUse: Boolean
        get() = true
        set(isDualUse) {
            super.isDualUse = isDualUse
        }

    fun release() {
        active = false
        for (entity in entityData.keys) {
            entity.removeTag("stasis")
            entity.deltaMovement = entityData[entity]!!.movement
        }
        entityData = HashMap()
    }

    override fun tick() {
        super.tick()
        if (active) {
            ++activeTicks
            for (entity in entityData.keys) {
                entity.setPos(entityData[entity]!!.position)
                entityData[entity]!!.addMovement(entity.deltaMovement)
                entity.deltaMovement = Vec3.ZERO
                val yaw = entityData[entity]!!.YRot
                val pitch = entityData[entity]!!.XRot


                // Apply new look angles
                entity.yRot = yaw
                entity.xRot = pitch
            }
        } else {
            activeTicks = 0
        }
        if (activeTicks > 1200) {
            dualActivate()
        }
    }

    fun addMovement(entity: Entity, movement: Vec3) {
        entityData[entity]!!.addMovement(movement)
    }

    inner class StasisEntityDataManager(movement: Vec3, position: Vec3, YRot: Float, XRot: Float) {
        var movement: Vec3
            private set
        var position: Vec3
        var YRot: Float
        var XRot: Float

        init {
            this.movement = Vec3(movement.x, 0.0, movement.z)
            addMovement(Vec3.ZERO)
            this.position = position
            this.YRot = YRot
            this.XRot = XRot
        }

        fun addMovement(push: Vec3) {
            val max = 6.0
            val d0 = max(-max, min(movement.x + push.x, max))
            val d1 = max(0.0, min(movement.y + push.y, 3.0))
            val d2 = max(-max, min(movement.z + push.z, max))

            this.movement = Vec3(d0, d1, d2)
        }
    }
}


