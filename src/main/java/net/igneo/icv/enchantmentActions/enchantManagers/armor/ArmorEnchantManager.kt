package net.igneo.icv.enchantmentActions.enchantManagers.armor

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player

abstract class ArmorEnchantManager protected constructor(
    type: EnchantType, // 60 is roughly 1 second
    @JvmField val maxCoolDown: Int, coolDownDamageBonus: Int, dualUse: Boolean, player: Player?
) :
    EnchantmentManager(type, player!!) {
    open var isDualUse: Boolean = false
    var coolDownDamageBonus: Int
    private var coolDown: Int

    init {
        this.coolDown = maxCoolDown
        this.coolDownDamageBonus = coolDownDamageBonus
        this.isDualUse = dualUse
    }


    fun getCoolDown(): Int {
        if (this.coolDown < 0) {
            this.coolDown = 0
        }
        if (this.coolDown > maxCoolDown) {
            this.coolDown = maxCoolDown
        }
        return coolDown
    }

    fun addCoolDown(coolDown: Int) {
        if (this.shouldTickCooldown()) {
            this.coolDown = this.coolDown + coolDown
            if (this.coolDown < 0) {
                this.coolDown = 0
                onOffCoolDown(player)
            }
            if (this.coolDown > maxCoolDown) {
                this.coolDown = maxCoolDown
            }
        }
    }

    open fun resetCoolDown() {
        this.coolDown = maxCoolDown
    }

    abstract fun onOffCoolDown(player: Player?)

    open val isOffCoolDown: Boolean
        get() = coolDown == 0

    fun tickCoolDown(player: Player?) {
        if (this.coolDown > 0 && shouldTickCooldown()) {
            --this.coolDown
            if (coolDown == 0) {
                onOffCoolDown(player)
            }
        }
    }

    open fun shouldTickCooldown(): Boolean {
        return canUse()
    }

    fun targetDamaged() {
        if (!isOffCoolDown) {
            addCoolDown(coolDownDamageBonus)
        }
    }

    override fun tick() {
        super.tick()
        tickCoolDown(player)
        if (isOffCoolDown) {
            whileOffCoolDown()
        }
    }

    fun whileOffCoolDown() {
    }

    override fun use() {
        println(canUse())
        if (canUse()) {
            if (isOffCoolDown) {
                activate()
                resetCoolDown()
            } else if (payBloodCost()) {
                activate()
                resetCoolDown()
            }
        } else if (isDualUse) {
            dualActivate()
        }
    }

    open fun dualActivate() {
    }

    override fun canUse(): Boolean {
        return true
    }

    fun payBloodCost(): Boolean {
        if (player!!.isCreative) {
            return false
        }
        val health = player!!.maxHealth * 0.5f
        val coolDownPercent = (coolDown.toFloat() / maxCoolDown) + 2
        val cost = health * coolDownPercent
        if (player!!.health > cost) {

            val level = player!!.level()
            if (player!!.level() is ServerLevel) {
                val damageSource: DamageSource = level.damageSources().magic()
                player!!.hurt(damageSource, cost)
            }
            return true
        }
        return false
    }

    abstract val indicator: EnchantIndicator?
}
