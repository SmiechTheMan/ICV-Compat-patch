package net.igneo.icv.enchantmentActions.enchantManagers.trident

import net.igneo.icv.Utils.getItemForSlot
import net.igneo.icv.Utils.getSlotForType
import net.igneo.icv.enchantment.EnchantType
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

abstract class TridentEnchantManager protected constructor(setSlot: EnchantType, player: Player?) :
    EnchantmentManager(setSlot, player!!) {
    @JvmField
    var trident: ThrownTrident? = null
    var tridentItem: ItemStack?


    init {
        val slot = getSlotForType(player!!, this.javaClass)
        this.tridentItem = getItemForSlot(player, slot)
    }

    fun onHit(result: HitResult?, trident: ThrownTrident?) {
    }

    fun overrideOnHit(): Boolean {
        return false
    }

    open fun onHitEntity(result: EntityHitResult?, trident: ThrownTrident?) {
    }

    fun overrideOnHitEntity(): Boolean {
        return false
    }

    open fun onHitBlock(result: BlockHitResult?, trident: ThrownTrident?) {
    }

    fun overrideOnHitBlock(): Boolean {
        return false
    }

    override fun canUse(): Boolean {
        return tridentThrown()
    }

    private fun tridentThrown(): Boolean {
        if (tridentItem != null && tridentItem!!.hasTag()) {
            val trident =
                Minecraft.getInstance().level!!.getEntity(tridentItem!!.tag!!.getInt("tridentID")) as ThrownTrident?
            if (!(trident == null || !trident.isAddedToWorld)) {
                this.trident = trident
                return true
            }
        }
        trident = null
        return false
    }

    override fun use() {
        if (this.canUse()) {
            activate()
        }
    }

    override fun onEquip() {
        // this will play a sound later
    }

    override fun activate() {
    }

    val damageBonus: Float
        get() = 0f

    open fun onRelease() {
    }
}
