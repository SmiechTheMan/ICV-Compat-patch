package net.igneo.icv.init

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraftforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

object Keybindings {
    private const val ICV_CATEGORY = "key.categories.icv.enchantments"

    val helmet: KeyMapping = KeyMapping(
        "key.icv.helmet",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y,
        ICV_CATEGORY
    )
    val chestplate: KeyMapping = KeyMapping(
        "key.icv.chestplate",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U,
        ICV_CATEGORY
    )
    val leggings: KeyMapping = KeyMapping(
        "key.icv.leggings",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I,
        ICV_CATEGORY
    )
    val boots: KeyMapping = KeyMapping(
        "key.icv.boots",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O,
        ICV_CATEGORY
    )
}
