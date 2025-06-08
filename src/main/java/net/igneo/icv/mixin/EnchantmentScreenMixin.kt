@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE") // For Mixin + Forge compatibility

package net.igneo.icv.mixin

import com.google.common.collect.Lists
import com.mojang.blaze3d.platform.Lighting
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.igneo.icv.ICV
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen
import net.minecraft.client.model.BookModel
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.EnchantmentMenu
import net.minecraft.world.item.enchantment.Enchantment
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite
import org.spongepowered.asm.mixin.Shadow

@Mixin(EnchantmentScreen::class)
abstract class EnchantmentScreenMixin(
    menu: EnchantmentMenu,
    inventory: Inventory,
    title: Component
) : AbstractContainerScreen<EnchantmentMenu>(menu, inventory, title) {

    @Shadow private lateinit var bookModel: BookModel
    @Shadow private var flip = 0f
    @Shadow private var oFlip = 0f
    @Shadow private var flipT = 0f
    @Shadow private var flipA = 0f
    @Shadow private var open = 0f
    @Shadow private var oOpen = 0f

    companion object {
        private val TABLE_TEX = ResourceLocation(ICV.MOD_ID, "textures/gui/container/enchanting_table.png")
        private val BOOK_TEX = ResourceLocation("textures/entity/enchanting_table_book.png")
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, delta: Double): Boolean {
        minecraft?.gameMode?.handleInventoryButtonClick(menu.containerId, if (delta < 0) -1 else -2)
        return super.mouseScrolled(mouseX, mouseY, delta)
    }

    /**
     * @author Igneo220
     * @reason enchantment revamp
     */
    @Overwrite
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val left = (width - imageWidth) / 2
        val top = (height - imageHeight) / 2

        for (i in 0..2) {
            val x = mouseX - (left + 60)
            val y = mouseY - (top + 14 + 19 * i)
            if (x in 0.0..108.0 && y in 0.0..19.0 && menu.clickMenuButton(minecraft?.player, i)) {
                minecraft?.gameMode?.handleInventoryButtonClick(menu.containerId, i)
                return true
            }
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    /**
     * @author Igneo220
     * @reason changing enchantment table
     */
    @Overwrite
    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val left = (width - imageWidth) / 2
        val top = (height - imageHeight) / 2

        graphics.blit(TABLE_TEX, left, top, 0, 0, imageWidth, imageHeight)
        renderBook(graphics, left, top, partialTick)

        val gold = menu.goldCount

        for (i in 0..2) {
            val x = left + 60
            val y = top + 14 + 19 * i
            val hasEnchant = menu.enchantClue[i] != -1

            if (!hasEnchant) {
                graphics.blit(TABLE_TEX, x, y, 0, 185, 108, 19)
                continue
            }

            val enchant = Enchantment.byId(menu.enchantClue[i])
            val label = Component.translatable("%s", enchant?.getFullname(menu.levelClue[i]) ?: "").withStyle(ChatFormatting.BLACK)
            val cost = 1
            val lapisX = x + 20
            val lapisY = y + 2
            val highlight = mouseX in x..(x + 107) && mouseY in y..(y + 18)

            when {
                (gold < i + 1 || (minecraft?.player?.experienceLevel ?: 0) < cost) && !minecraft?.player?.abilities?.instabuild!! -> {
                    graphics.blit(TABLE_TEX, x, y, 0, 185, 108, 19)
                }
                highlight -> {
                    graphics.blit(TABLE_TEX, x, y, 0, 204, 108, 19)
                    graphics.blit(TABLE_TEX, x + 1, y + 1, 16 * i, 223, 16, 16)
                    graphics.drawString(font, label.string, lapisX, lapisY + 12, -0x23000001, true)
                }
                else -> {
                    graphics.blit(TABLE_TEX, x, y, 0, 166, 108, 19)
                    graphics.blit(TABLE_TEX, x + 1, y + 1, 16 * i, 223, 16, 16)
                    graphics.drawString(font, label.string, lapisX, lapisY + 12, 8453920, true)
                }
            }
        }
    }

    @Shadow
    private fun renderBook(graphics: GuiGraphics, x: Int, y: Int, partialTick: Float) {
        val openInterp = Mth.lerp(partialTick, oOpen, open)
        val flipInterp = Mth.lerp(partialTick, oFlip, flip)
        Lighting.setupForEntityInInventory()
        graphics.pose().apply {
            pushPose()
            translate(x + 33f, y + 31f, 100f)
            scale(-40f, 40f, 40f)
            mulPose(Axis.XP.rotationDegrees(25f))
            translate((1 - openInterp) * 0.2f, (1 - openInterp) * 0.1f, (1 - openInterp) * 0.25f)
            mulPose(Axis.YP.rotationDegrees(-(1 - openInterp) * 90 - 90))
            mulPose(Axis.XP.rotationDegrees(180f))
        }

        val f4 = Mth.clamp(Mth.frac(flipInterp + 0.25f) * 1.6f - 0.3f, 0f, 1f)
        val f5 = Mth.clamp(Mth.frac(flipInterp + 0.75f) * 1.6f - 0.3f, 0f, 1f)

        bookModel.setupAnim(0f, f4, f5, openInterp)
        val consumer: VertexConsumer = graphics.bufferSource().getBuffer(bookModel.renderType(BOOK_TEX))
        bookModel.renderToBuffer(graphics.pose(), consumer, 15728880, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f)
        graphics.flush()
        graphics.pose().popPose()
        Lighting.setupFor3DItems()
    }

    /**
     * @author Igneo220
     * @reason render tooltips
     */
    @Overwrite
    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(graphics)
        super.render(graphics, mouseX, mouseY, minecraft!!.frameTime)
        renderTooltip(graphics, mouseX, mouseY)

        val canCheat = minecraft!!.player?.abilities?.instabuild == true

        for (i in 0..2) {
            val x = 60
            val y = 14 + 19 * i
            if (!isHovering(x, y, 108, 17, mouseX.toDouble(), mouseY.toDouble()) || menu.costs[i] <= 0) continue

            val clues = menu.enchantClue
            val levels = menu.levelClue
            val enchant = Enchantment.byId(clues[i])
            val components = Lists.newArrayList<Component>()

            if (enchant != null) {
                val key = BuiltInRegistries.ENCHANTMENT.getKey(enchant).toString().replace(":", ".").replace(" ", "_")
                components.add(Component.translatable("enchantment.$key.desc", enchant.getFullname(levels[i])).withStyle(ChatFormatting.WHITE))
            }

            if (enchant != null && !canCheat) {
                val lapis = Component.translatable("container.enchant.lapis.${if (i + 1 == 1) "one" else "many"}", i + 1)
                val level = Component.translatable("container.enchant.level.${if (i + 1 == 1) "one" else "many"}", i + 1)
                // Add these components to the tooltip if desired
            }

            graphics.renderComponentTooltip(font, components, mouseX, mouseY)
            break
        }
    }
}