package net.igneo.icv.enchantmentActions

import net.igneo.icv.client.indicators.EnchantIndicator
import net.igneo.icv.enchantment.EnchantType.Companion.applicableSlot
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.loading.FMLEnvironment

class PlayerEnchantmentActions {

    /*
    0=boots
    1=leggings
    2=chestplate
    3=helmet
    4=mainhand
    5=offhand
    */

    @JvmField
    val managers: Array<EnchantmentManager?> = arrayOfNulls(6)

    fun getManager(slot: Int): EnchantmentManager? {
        return managers[slot]
    }

    @JvmField
    var animated: Boolean = false

    fun setManager(manager: EnchantmentManager?, slot: Int) {
        val current = managers.getOrNull(slot)

        if (manager != null && !applicableSlot(manager.type, slot)) {
            current?.onRemove()
            managers[slot] = null
            return
        }

        if (current != null && manager != null && current::class == manager::class) return

        current?.onRemove()
        managers[slot] = manager
        manager?.onEquip()

        if (slot < 4 && FMLEnvironment.dist.isClient) {
            indicators[slot] = (manager as? ArmorEnchantManager)?.indicator
        }
    }

    @OnlyIn(Dist.CLIENT)
    var indicators: Array<EnchantIndicator?> = arrayOfNulls(4)

    @JvmField
    var input: Input = Input.FORWARD

    fun copyFrom(source: PlayerEnchantmentActions?) {
    }

    fun saveNBTData(nbt: CompoundTag?) {
    }

    fun loadNBTData(nbt: CompoundTag?) {
    }
}
