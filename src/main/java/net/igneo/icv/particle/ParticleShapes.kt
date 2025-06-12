package net.igneo.icv.particle

import net.igneo.icv.Utils.UP_VECTOR
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun renderLine(level: ServerLevel, start: Vec3, end: Vec3, particle: ParticleOptions, points: Int) {
    processLine(start, end, points) { point ->
        level.sendParticles(particle, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0)
    }
}

fun renderLineList(level: Level?, start: Vec3, end: Vec3, points: Int): List<Vec3> =
    buildList {
        processLine(start, end, points) { add(it) }
    }

fun renderRing(level: ServerLevel, center: Vec3, particle: ParticleOptions, points: Int, radius: Float) {
    processRing(center, points, radius) { point ->
        level.sendParticles(particle, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0)
    }
}

fun renderRingList(level: Level?, center: Vec3, points: Int, radius: Float): List<Vec3> =
    buildList {
        processRing(center, points, radius) { add(it) }
    }

fun renderSphere(
    level: ServerLevel,
    center: Vec3,
    particle: ParticleOptions,
    latitudeSteps: Int,
    longitudeSteps: Int,
    radius: Float
) {
    processSphere(center, latitudeSteps, longitudeSteps, radius) { point ->
        level.sendParticles(particle, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0)
    }
}

fun renderSphereList(
    level: Level?,
    center: Vec3,
    latitudeSteps: Int,
    longitudeSteps: Int,
    radius: Float
): List<Vec3> = buildList {
    processSphere(center, latitudeSteps, longitudeSteps, radius) { add(it) }
}

fun renderCone(
    level: ServerLevel,
    player: Player,
    direction: Vec3,
    particle: ParticleOptions,
    radius: Double,
    halfAngleDegrees: Double,
    particlesPerRing: Int,
    numRings: Int
) {
    processCone(player, direction, radius, halfAngleDegrees, particlesPerRing, numRings) { point ->
        level.sendParticles(particle, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0)
    }
}

fun renderConeList(
    level: Level?,
    player: Player,
    direction: Vec3,
    radius: Double,
    halfAngleDegrees: Double,
    particlesPerRing: Int,
    numRings: Int
): List<Vec3> = buildList {
    processCone(player, direction, radius, halfAngleDegrees, particlesPerRing, numRings) { add(it) }
}

// Internal Geometry Processing Functions

private fun processLine(start: Vec3, end: Vec3, points: Int, pointConsumer: (Vec3) -> Unit) {
    val dx = (end.x - start.x) / points
    val dy = (end.y - start.y) / points
    val dz = (end.z - start.z) / points

    for (i in 0..points) {
        val x = start.x + dx * i
        val y = start.y + dy * i
        val z = start.z + dz * i
        pointConsumer(Vec3(x, y, z))
    }
}

private fun processRing(center: Vec3, points: Int, radius: Float, pointConsumer: (Vec3) -> Unit) {
    val angleStep = 2 * PI / points
    for (i in 0 until points) {
        val angle = i * angleStep
        val x = center.x + radius * cos(angle)
        val z = center.z + radius * sin(angle)
        pointConsumer(Vec3(x, center.y, z))
    }
}

private fun processSphere(
    center: Vec3,
    latitudeSteps: Int,
    longitudeSteps: Int,
    radius: Float,
    pointConsumer: (Vec3) -> Unit
) {
    val latStep = PI / latitudeSteps
    val lonStep = 2 * PI / longitudeSteps

    for (i in 0..latitudeSteps) {
        val latAngle = i * latStep
        val sinLat = sin(latAngle)
        val cosLat = cos(latAngle)

        for (j in 0 until longitudeSteps) {
            val lonAngle = j * lonStep
            val x = center.x + radius * sinLat * cos(lonAngle)
            val y = center.y + radius * cosLat
            val z = center.z + radius * sinLat * sin(lonAngle)
            pointConsumer(Vec3(x, y, z))
        }
    }
}

private fun processCone(
    player: Player,
    direction: Vec3,
    radius: Double,
    halfAngleDegrees: Double,
    particlesPerRing: Int,
    numRings: Int,
    pointConsumer: (Vec3) -> Unit
) {
    val origin = player.position().add(UP_VECTOR)
    val tanHalfAngle = tan(Math.toRadians(halfAngleDegrees))
    val angleStep = 2 * PI / particlesPerRing

    val up = Vec3(0.0, 1.0, 0.0)
    var right = direction.cross(up).normalize()
    if (right.length() < 0.001) right = Vec3(1.0, 0.0, 0.0)
    val newUp = right.cross(direction).normalize()

    for (ring in 1..numRings) {
        val ringDistance = radius * ring / numRings
        val circleRadius = tanHalfAngle * ringDistance
        val ringCenter = origin.add(direction.scale(ringDistance))

        for (i in 0 until particlesPerRing) {
            val angle = i * angleStep
            val offset = right.scale(cos(angle) * circleRadius)
                .add(newUp.scale(sin(angle) * circleRadius))

            pointConsumer(ringCenter.add(offset))
        }
    }
}