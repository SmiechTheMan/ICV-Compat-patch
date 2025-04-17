package net.igneo.icv.entity.boots.surfWave;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.leggings.wave.WaveEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class SurfWaveEntity extends ICVEntity {
    public SurfWaveEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    private int lifetime = 0;
    private Vec3 storedMotion = Vec3.ZERO;
    
    @Override
    public float getStepHeight() {
        return 1.5F;
    }
    
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    
    @Override
    protected <E extends GeoEntity> PlayState animController(AnimationState<E> event) {
        return event.setAndContinue(IDLE_ANIM);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.getPassengers().isEmpty())
            storedMotion = ICVUtils.getFlatDirection(this.getFirstPassenger().getYRot(), 0.4F, 0);
        for (Entity entity : this.level().getEntities(null, this.getBoundingBox().inflate(3))) {
            if (entity != this.getOwner() && !(entity instanceof WaveEntity) && !(entity instanceof SurfWaveEntity)) {
                entity.addDeltaMovement(storedMotion);
            }
        }
        setDeltaMovement(storedMotion);
        if (lifetime < 300) {
            ++lifetime;
        } else {
            this.discard();
        }
        faceDirection(this.getDeltaMovement());
    }
    
    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return pPassenger == this.getOwner();
    }
}
