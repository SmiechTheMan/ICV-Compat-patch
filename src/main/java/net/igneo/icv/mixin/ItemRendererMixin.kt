package net.igneo.icv.mixin

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemInHandRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ItemInHandRenderer::class)
class ItemRendererMixin {
    @Inject(method = ["renderItem"], at = [At("HEAD")], cancellable = true)
    private fun onRenderItem(
        pEntity: LivingEntity,
        pItemStack: ItemStack,
        pDisplayContext: ItemDisplayContext,
        pLeftHand: Boolean,
        pPoseStack: PoseStack,
        pBuffer: MultiBufferSource,
        pSeed: Int,
        ci: CallbackInfo
    ) {
        if (pItemStack.hasTag()) {
            val entity = Minecraft.getInstance().level!!.getEntity(
                pItemStack.tag!!.getInt("tridentID")
            )
            if (entity is ThrownTrident) {
                if (!(entity == null || !entity.isAddedToWorld())) {
                    ci.cancel()
                }
            } else {
                pItemStack.removeTagKey("tridentID")
            }
        }
    }
}
