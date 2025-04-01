package net.igneo.icv.entity.wave;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.surfWave.SurfWaveEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WaveEntity extends ICVEntity {
    public WaveEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    private int lifetime = 0;
    private Vec3 storedMotion = Vec3.ZERO;
    public void setTrajectory(Vec3 vec) {
        storedMotion = vec;
    }

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    @Override
    public void tick() {
        super.tick();
        for (Entity entity : this.level().getEntities(null,this.getBoundingBox().inflate(3))) {
            if (!(entity instanceof WaveEntity) && !(entity instanceof SurfWaveEntity)) {
                entity.addDeltaMovement(storedMotion);
            }
        }
        setDeltaMovement(storedMotion);
        if (lifetime < 20) {
            ++lifetime;
        } else {
            this.discard();
        }
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return pPassenger == this.getOwner();
    }
}
