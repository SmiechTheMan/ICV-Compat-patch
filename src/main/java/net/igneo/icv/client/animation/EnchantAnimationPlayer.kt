package net.igneo.icv.client.animation

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.core.data.KeyframeAnimation

class EnchantAnimationPlayer : KeyframeAnimationPlayer {

    constructor(animation: KeyframeAnimation, tick: Int, mutable: Boolean) : super(animation, tick, mutable)
    constructor(animation: KeyframeAnimation) : super(animation, 0, false)

    fun isWindingDown(tickDelta: Float): Boolean {
        val windDownStart = data.endTick + (data.stopTick - data.endTick) / 4
        return (tick + tickDelta) > (windDownStart + 0.5f)
    }

    override fun getFirstPersonMode(tickDelta: Float): FirstPersonMode = FirstPersonMode.THIRD_PERSON_MODEL
}