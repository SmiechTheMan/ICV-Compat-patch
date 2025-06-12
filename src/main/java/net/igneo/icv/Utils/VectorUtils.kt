package net.igneo.icv.Utils

import net.igneo.icv.enchantmentActions.Input
import net.igneo.icv.enchantmentActions.Input.Companion.getRotation
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

fun getFlatInputDirection(rot: Float, input: Input, scale: Float, yVelocity: Double): Vec3 {
    val rotation = getRotation(input)
    val yaw = Math.toRadians((rot + rotation).toDouble())
    return Vec3(-sin(yaw) * scale, yVelocity, cos(yaw) * scale)
}

fun getFlatDirection(rot: Float, scale: Float, yVelocity: Double): Vec3 {
    val yaw = Math.toRadians(rot.toDouble())
    return Vec3(-sin(yaw) * scale, yVelocity, cos(yaw) * scale)
}

fun raycastGround(world: Level, start: Vec3, direction: Vec3, maxDistance: Double): HitResult? {
    if (world.isClientSide) return null // Server-side only

    val end = start.add(direction.normalize().scale(maxDistance))

    val context = ClipContext(
        start,
        end,
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        null
    )

    return world.clip(context)
}

val DOWN_VECTOR = Vec3(0.0, -1.0, 0.0)
val UP_VECTOR = Vec3(0.0, 1.0, 0.0)
val FORWARD_VECTOR = Vec3(0.0, 0.0, 1.0)
val BACKWARD_VECTOR = Vec3(0.0, 0.0, -1.0)
val LEFT_VECTOR = Vec3(-1.0, 0.0, 0.0)
val RIGHT_VECTOR = Vec3(1.0, 0.0, 0.0)
