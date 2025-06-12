package net.igneo.icv.init

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraftforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

private const val ICV_CATEGORY = "key.categories.icv.enchantments"

@JvmField
val helmet: KeyMapping = KeyMapping(
    "key.icv.helmet",
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y,
    ICV_CATEGORY
)

@JvmField
val chestplate: KeyMapping = KeyMapping(
    "key.icv.chestplate",
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U,
    ICV_CATEGORY
)

@JvmField
val leggings: KeyMapping = KeyMapping(
    "key.icv.leggings",
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I,
    ICV_CATEGORY
)

@JvmField
val boots: KeyMapping = KeyMapping(
    "key.icv.boots",
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O,
    ICV_CATEGORY
)
