package net.igneo.icv.enchantmentActions

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.common.util.LazyOptional

class PlayerEnchantmentActionsProvider : ICapabilityProvider, INBTSerializable<CompoundTag> {
    private var enchNBT: PlayerEnchantmentActions? = null
    private val optional = LazyOptional.of { createEnchantmentActions() }

    private fun createEnchantmentActions() = enchNBT ?: PlayerEnchantmentActions().also { enchNBT = it }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> =
        if (cap === PLAYER_ENCHANTMENT_ACTIONS) optional.cast() else LazyOptional.empty()

    override fun serializeNBT(): CompoundTag = CompoundTag().also {
        createEnchantmentActions().saveNBTData(it)
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        createEnchantmentActions().loadNBTData(nbt)
    }

    companion object {
        val PLAYER_ENCHANTMENT_ACTIONS: Capability<PlayerEnchantmentActions> =
            CapabilityManager.get(object : CapabilityToken<PlayerEnchantmentActions>() {})
    }
}
