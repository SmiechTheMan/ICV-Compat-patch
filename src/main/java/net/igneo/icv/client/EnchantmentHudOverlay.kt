package net.igneo.icv.client

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.client.gui.overlay.IGuiOverlay

@JvmField
var animTime: Long = 0

@JvmField
val HUD_ENCHANTMENTS: IGuiOverlay = IGuiOverlay { _, graphics: GuiGraphics, _, width: Int, height: Int ->
    val x = width / 2
    val player = Minecraft.getInstance().player ?: return@IGuiOverlay

    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent { actions ->
        for (slot in 0..3) {
            val indicator = actions.indicators[slot] ?: continue
            var offset = 16 * indicator.slot
            if (offset > 0) offset++

            val posX = x - 94 + offset
            val posY = height - 65

            if (indicator.shouldRender()) {
                graphics.blit(
                    indicator.image,
                    posX,
                    posY,
                    0f,
                    (16 * indicator.currentFrame).toFloat(),
                    16,
                    16,
                    16,
                    indicator.height
                )
            }
        }
    }

    if (animTime == 0L) animTime = System.currentTimeMillis()
    if (System.currentTimeMillis() >= animTime + 125) {
        animTime = System.currentTimeMillis()
    }
}