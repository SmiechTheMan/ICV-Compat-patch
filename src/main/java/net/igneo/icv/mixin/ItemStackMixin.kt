package net.igneo.icv.mixin

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [ItemStack::class])
abstract class ItemStackMixin {
    @get:Shadow
    abstract val isEnchantable: Boolean

    @get:Shadow
    abstract val isEnchanted: Boolean

    @Inject(method = ["enchant"], at = [At("HEAD")], cancellable = true)
    private fun `enchancement$enchantmentLimit`(pEnchantment: Enchantment?, level: Int, ci: CallbackInfo) {
        if (pEnchantment == null || this.isEnchanted) {
            ci.cancel()
        }
    }
}
