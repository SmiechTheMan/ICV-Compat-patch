package net.igneo.icv.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(LivingEntity pEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pSeed, CallbackInfo ci) {
        if (pItemStack.hasTag()) {
            Entity entity = Minecraft.getInstance().level.getEntity(pItemStack.getTag().getInt("tridentID"));
            if (entity instanceof ThrownTrident trident) {
                if (!(trident == null || !trident.isAddedToWorld())) {
                    ci.cancel();
                }
            } else {
                pItemStack.removeTagKey("tridentID");
            }
        }
    }
}
