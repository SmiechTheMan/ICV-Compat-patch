package net.igneo.icv.init

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.function.Consumer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object ParticleShapes {
    fun renderLine(level: ServerLevel, start: Vec3, end: Vec3, particle: ParticleOptions, points: Int) {
        processLine(
            start, end, points
        ) { point: Vec3 ->
            level.sendParticles(
                particle,
                point.x,
                point.y,
                point.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
            )
        }
    }

    @JvmStatic
    fun renderLineList(level: Level?, start: Vec3, end: Vec3, points: Int): ArrayList<Vec3> {
        val posList = ArrayList<Vec3>(points + 1)
        processLine(start, end, points) { e: Vec3 -> posList.add(e) }
        return posList
    }

    fun renderRing(level: ServerLevel, center: Vec3, particle: ParticleOptions, points: Int, radius: Float) {
        processRing(
            center, points, radius
        ) { point: Vec3 ->
            level.sendParticles(
                particle,
                point.x,
                point.y,
                point.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
            )
        }
    }

    fun renderRingList(level: Level?, center: Vec3, points: Int, radius: Float): ArrayList<Vec3> {
        val posList = ArrayList<Vec3>(points * 2)
        processRing(center, points, radius) { e: Vec3 -> posList.add(e) }
        return posList
    }

    fun renderSphere(
        level: ServerLevel,
        center: Vec3,
        particle: ParticleOptions,
        latitudeSteps: Int,
        longitudeSteps: Int,
        radius: Float
    ) {
        processSphere(
            center, latitudeSteps, longitudeSteps, radius
        ) { point: Vec3 ->
            level.sendParticles(
                particle,
                point.x,
                point.y,
                point.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
            )
        }
    }

    fun renderSphereList(
        level: Level?,
        center: Vec3,
        latitudeSteps: Int,
        longitudeSteps: Int,
        radius: Float
    ): ArrayList<Vec3> {
        val estimatedSize = (latitudeSteps + 1) * longitudeSteps * 4
        val posList = ArrayList<Vec3>(estimatedSize)
        processSphere(
            center, latitudeSteps, longitudeSteps, radius
        ) { e: Vec3 -> posList.add(e) }
        return posList
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
        processCone(
            player, direction, radius, halfAngleDegrees, particlesPerRing, numRings
        ) { point: Vec3 ->
            level.sendParticles(
                particle,
                point.x,
                point.y,
                point.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
            )
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
    ): ArrayList<Vec3> {
        val estimatedSize = particlesPerRing * numRings
        val posList = ArrayList<Vec3>(estimatedSize)
        processCone(
            player, direction, radius, halfAngleDegrees, particlesPerRing, numRings
        ) { e: Vec3 -> posList.add(e) }
        return posList
    }

    // Math stuff below
    private fun processLine(start: Vec3, end: Vec3, points: Int, pointConsumer: Consumer<Vec3>) {
        val dx = (end.x - start.x) / points
        val dy = (end.y - start.y) / points
        val dz = (end.z - start.z) / points

        for (i in 0..points) {
            val x = start.x + dx * i
            val y = start.y + dy * i
            val z = start.z + dz * i

            pointConsumer.accept(Vec3(x, y, z))
        }
    }

    private fun processRing(center: Vec3, points: Int, radius: Float, pointConsumer: Consumer<Vec3>) {
        val angleStep = Math.PI / points

        for (i in 0 until points) {
            val angleInt = TrigMath.radiansToInternalAngle(2 * i * angleStep)

            val trigValues = TrigMath.sincos(angleInt)
            val sinValue = trigValues[0]
            val cosValue = trigValues[1]

            val x = (center.x + radius * cosValue).toFloat()
            val z = (center.z + radius * sinValue).toFloat()
            val inv_x = (center.x - radius * cosValue).toFloat()
            val inv_z = (center.z - radius * sinValue).toFloat()

            pointConsumer.accept(Vec3(x.toDouble(), center.y, z.toDouble()))
            pointConsumer.accept(Vec3(inv_x.toDouble(), center.y, inv_z.toDouble()))
        }
    }

    private fun processSphere(
        center: Vec3,
        latitudeSteps: Int,
        longitudeSteps: Int,
        radius: Float,
        pointConsumer: Consumer<Vec3>
    ) {
        val latStep = Math.PI / (latitudeSteps * 0.25)
        val lonStep = Math.PI / (longitudeSteps * 0.25)

        for (i in 0..latitudeSteps) {
            val latAngleInt = TrigMath.radiansToInternalAngle(i * latStep)
            val latTrig = TrigMath.sincos(latAngleInt)
            val sinLat = latTrig[0]
            val cosLat = latTrig[1]

            for (j in 0 until longitudeSteps) {
                val lonAngleInt = TrigMath.radiansToInternalAngle(j * lonStep)
                val lonTrig = TrigMath.sincos(lonAngleInt)
                val sinLon = lonTrig[0]
                val cosLon = lonTrig[1]

                val x = (center.x + radius * sinLat * cosLon).toFloat()
                val baseLat = (radius * cosLat).toFloat()
                val baseLon = (radius * sinLat * sinLon).toFloat()

                pointConsumer.accept(Vec3(x.toDouble(), center.y + baseLat, center.z + baseLon))
                pointConsumer.accept(Vec3(x.toDouble(), center.y - baseLat, center.z + baseLon))
                pointConsumer.accept(Vec3(x.toDouble(), center.y - baseLat, center.z - baseLon))
                pointConsumer.accept(Vec3(x.toDouble(), center.y + baseLat, center.z - baseLon))
            }
        }
    }

    // (WIP) Still need to convert to custom sincos function
    private fun processCone(
        player: Player,
        direction: Vec3,
        radius: Double,
        halfAngleDegrees: Double,
        particlesPerRing: Int,
        numRings: Int,
        pointConsumer: Consumer<Vec3>
    ) {
        val playerPos = player.position().add(ICVUtils.UP_VECTOR)
        val tanHalfAngle = tan(Math.toRadians(halfAngleDegrees))
        val angleStep = 2 * Math.PI / particlesPerRing

        val up = Vec3(0.0, 1.0, 0.0)
        var right = direction.cross(up).normalize()
        if (right.length() < 0.001f) {
            right = Vec3(1.0, 0.0, 0.0)
        }
        val newUp = right.cross(direction).normalize()

        for (ring in 1..numRings) {
            val ringDistance = (radius * ring) / numRings
            val circleRadius = tanHalfAngle * ringDistance
            val ringCenter = playerPos.add(direction.scale(ringDistance))

            for (i in 0 until particlesPerRing) {
                val angle = i * angleStep
                val cosAngle = cos(angle)
                val sinAngle = sin(angle)

                val offset = right.scale(cosAngle * circleRadius)
                    .add(newUp.scale(sinAngle * circleRadius))

                val particlePos = ringCenter.add(offset)
                pointConsumer.accept(particlePos)
            }
        }
    }
}