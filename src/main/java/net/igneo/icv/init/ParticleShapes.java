package net.igneo.icv.init;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.function.Consumer;

import static net.igneo.icv.init.TrigMath.*;

public class ParticleShapes {
    public static void renderLine(ServerLevel level, Vec3 start, Vec3 end, ParticleOptions particle, int points) {
        double dx = (end.x - start.x) / points;
        double dy = (end.y - start.y) / points;
        double dz = (end.z - start.z) / points;

        for (int i = 0; i <= points; i++) {
            double x = start.x + dx * i;
            double y = start.y + dy * i;
            double z = start.z + dz * i;

            level.sendParticles(
                    particle,
                    x, y, z,
                    1, 0, 0, 0, 0
            );
        }
    }

    public static void renderRing(ServerLevel level, Vec3 center, ParticleOptions particle, int points, float radius) {

        for (int i = 0; i < points; i++) {
            int angleInt = radiansToInternalAngle(2 * Math.PI * i / points);

            double[] trigValues = sincos(angleInt);
            double sinValue = trigValues[0];
            double cosValue = trigValues[1];

            float x = (float) (center.x + radius * cosValue);
            float z = (float) (center.z + radius * sinValue);

            level.sendParticles(
                    particle,
                    x, center.y, z,
                    1, 0, 0, 0, 0
            );
        }
    }

    public static void renderSphere(ServerLevel level, Vec3 center, ParticleOptions particle, int latitudeSteps, int longitudeSteps, float radius) {

        for (int i = 0; i <= latitudeSteps; i++) {
            int latAngleInt = radiansToInternalAngle(Math.PI * i / latitudeSteps);
            double[] latTrig = sincos(latAngleInt);
            double sinLat = latTrig[0];
            double cosLat = latTrig[1];

            for (int j = 0; j < longitudeSteps; j++) {
                int lonAngleInt = radiansToInternalAngle(2 * Math.PI * j / longitudeSteps);
                double[] lonTrig = sincos(lonAngleInt);
                double sinLon = lonTrig[0];
                double cosLon = lonTrig[1];

                float x = (float) (center.x + radius * sinLat * cosLon);
                float y = (float) (center.y + radius * cosLat);
                float z = (float) (center.z + radius * sinLat * sinLon);

                level.sendParticles(
                        particle,
                        x, y, z,
                        1, 0, 0, 0, 0
                );
            }
        }
    }

    public static void renderCone(ServerLevel level, Player player, Vec3 direction, ParticleOptions particle, double radius, double halfAngleDegrees, int particlesPerRing, int numRings) {
        Vec3 playerPos = player.position().add(ICVUtils.UP_VECTOR);

        int halfAngleInt = radiansToInternalAngle(Math.toRadians(halfAngleDegrees));
        double tangentHalfAngle = tan(halfAngleInt);

        for (int ring = 1; ring <= numRings; ring++) {
            double ringDistance = (radius * ring) / numRings;

            double circleRadius = tangentHalfAngle * ringDistance;

            for (int i = 0; i < particlesPerRing; i++) {
                int angleInt = radiansToInternalAngle(2 * Math.PI * i / particlesPerRing);
                double[] trigValues = sincos(angleInt);
                double sinAngle = trigValues[0];
                double cosAngle = trigValues[1];

                Vec3 right = direction.cross(ICVUtils.UP_VECTOR).normalize();
                if (right.length() < 0.001f) {
                    right = ICVUtils.RIGHT_VECTOR;
                }
                Vec3 newUp = right.cross(direction).normalize();

                Vec3 offset = right.scale(cosAngle * circleRadius)
                        .add(newUp.scale(sinAngle * circleRadius));

                Vec3 particlePos = playerPos.add(direction.scale(ringDistance)).add(offset);

                level.sendParticles(
                        particle,
                        particlePos.x, particlePos.y, particlePos.z,
                        1, 0, 0, 0, 0
                );
            }
        }
    }





    public static ArrayList<Vec3> renderLineList(Level level, Vec3 start, Vec3 end, int points) {
        double dx = (end.x - start.x) / points;
        double dy = (end.y - start.y) / points;
        double dz = (end.z - start.z) / points;
        ArrayList<Vec3> posList = new ArrayList<>();

        for (int i = 0; i <= points; i++) {
            double x = start.x + dx * i;
            double y = start.y + dy * i;
            double z = start.z + dz * i;

            posList.add(new Vec3(x,y,z));
        }

        return posList;
    }

    public static ArrayList<Vec3> renderRingList(Level level, Vec3 center, int points, float radius) {
        ArrayList<Vec3> posList = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            int angleInt = radiansToInternalAngle(2 * Math.PI * i / points);

            double[] trigValues = sincos(angleInt);
            double sinValue = trigValues[0];
            double cosValue = trigValues[1];

            float x = (float) (center.x + radius * cosValue);
            float z = (float) (center.z + radius * sinValue);

            posList.add(new Vec3(x,center.y,z));
        }
        return posList;
    }

    public static ArrayList<Vec3> renderSphereList(Level level, Vec3 center, int latitudeSteps, int longitudeSteps, float radius) {
        ArrayList<Vec3> posList = new ArrayList<>();
        for (int i = 0; i <= latitudeSteps; i++) {
            int latAngleInt = radiansToInternalAngle(Math.PI * i / latitudeSteps);
            double[] latTrig = sincos(latAngleInt);
            double sinLat = latTrig[0];
            double cosLat = latTrig[1];

            for (int j = 0; j < longitudeSteps; j++) {
                int lonAngleInt = radiansToInternalAngle(2 * Math.PI * j / longitudeSteps);
                double[] lonTrig = sincos(lonAngleInt);
                double sinLon = lonTrig[0];
                double cosLon = lonTrig[1];

                float x = (float) (center.x + radius * sinLat * cosLon);
                float y = (float) (center.y + radius * cosLat);
                float z = (float) (center.z + radius * sinLat * sinLon);

                posList.add(new Vec3(x,y,z));
            }
        }
        return posList;
    }

    public static ArrayList<Vec3> renderConeList(Level level, Player player, Vec3 direction, double radius, double halfAngleDegrees, int particlesPerRing, int numRings) {
        Vec3 playerPos = player.position().add(ICVUtils.UP_VECTOR);
        ArrayList<Vec3> posList = new ArrayList<>();
        int halfAngleInt = radiansToInternalAngle(Math.toRadians(halfAngleDegrees));
        double tangentHalfAngle = tan(halfAngleInt);

        for (int ring = 1; ring <= numRings; ring++) {
            double ringDistance = (radius * ring) / numRings;

            double circleRadius = tangentHalfAngle * ringDistance;

            for (int i = 0; i < particlesPerRing; i++) {
                int angleInt = radiansToInternalAngle(2 * Math.PI * i / particlesPerRing);
                double[] trigValues = sincos(angleInt);
                double sinAngle = trigValues[0];
                double cosAngle = trigValues[1];

                Vec3 right = direction.cross(ICVUtils.UP_VECTOR).normalize();
                if (right.length() < 0.001f) {
                    right = ICVUtils.RIGHT_VECTOR;
                }
                Vec3 newUp = right.cross(direction).normalize();

                Vec3 offset = right.scale(cosAngle * circleRadius)
                        .add(newUp.scale(sinAngle * circleRadius));

                Vec3 particlePos = playerPos.add(direction.scale(ringDistance)).add(offset);

                posList.add(new Vec3(particlePos.x,particlePos.y,particlePos.z));
            }
        }
        return posList;
    }
}
