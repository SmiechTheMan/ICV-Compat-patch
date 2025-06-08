package net.igneo.icv.mixin

import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import net.igneo.icv.ICV
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [Minecraft::class])
class MinecraftMixin {
    @Inject(method = ["handleKeybinds"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun handleKeybinds(ci: CallbackInfo) {
        val animation =
            PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player!!)[ResourceLocation(
                ICV.MOD_ID,
                "enchant_animator"
            )] as ModifierLayer<IAnimation>?
        if (animation != null) {
            if (animation.isActive) ci.cancel()
        }
    }
}
