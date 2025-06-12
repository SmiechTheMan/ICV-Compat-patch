package net.igneo.icv.client.indicators

import net.igneo.icv.ICV
import net.igneo.icv.client.animTime
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager
import net.minecraft.resources.ResourceLocation
import kotlin.math.abs

abstract class EnchantIndicator(
    val totalFrames: Int,
    val chargeFrames: Int,
    private val loopFrame: Int,
    val slot: Int,
    val image: ResourceLocation,
    val manager: ArmorEnchantManager?
) {

    var currentFrame: Int = 0

    val height: Int get() = totalFrames * 16

    fun getFrame(): Int {
        manager ?: return 0

        if (currentFrame < chargeFrames || manager.getCoolDown() > 0) {
            val percent = manager.getCoolDown().toDouble() / manager.maxCoolDown
            currentFrame = (chargeFrames * abs(percent - 1)).toInt()
        } else if (System.currentTimeMillis() >= animTime + 125 && manager.canUse()) {
            currentFrame++
            if (currentFrame >= totalFrames) currentFrame = loopFrame
        }

        if (!manager.canUse()) currentFrame = 0

        return currentFrame
    }

    fun shouldRender(): Boolean = manager?.canUse()?.not() == true || currentFrame <= totalFrames
}

const val PATH: String = "textures/gui/enchantments/"

class BlackHoleIndicator(manager: ArmorEnchantManager?) : EnchantIndicator(
    totalFrames = 27,
    chargeFrames = 13,
    loopFrame = 17,
    slot = 0,
    image = ResourceLocation(ICV.MOD_ID, "${PATH}black_hole.png"),
    manager = manager
)

class BlinkCooldownIndicator(manager: ArmorEnchantManager?) : EnchantIndicator(
    totalFrames = 24,
    chargeFrames = 16,
    loopFrame = 20,
    slot = 3,
    image = ResourceLocation(ICV.MOD_ID, "${PATH}blink.png"),
    manager = manager
)

class StasisCooldownIndicator(manager: ArmorEnchantManager?) : EnchantIndicator(
    totalFrames = 41,
    chargeFrames = 14,
    loopFrame = 18,
    slot = 3,
    image = ResourceLocation(ICV.MOD_ID, "${PATH}stasis.png"),
    manager = manager
)

class SurfIndicator(manager: ArmorEnchantManager?) : EnchantIndicator(
    totalFrames = 28,
    chargeFrames = 15,
    loopFrame = 22,
    slot = 3,
    image = ResourceLocation(ICV.MOD_ID, "${PATH}surf.png"),
    manager = manager
)
