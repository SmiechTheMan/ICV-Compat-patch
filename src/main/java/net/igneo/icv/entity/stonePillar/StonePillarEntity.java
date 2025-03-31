package net.igneo.icv.entity.stonePillar;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StonePillarEntity extends ICVEntity {
    public StonePillarEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    private int lifetime = 0;
    float health = 40;

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.health <= 0) {
            this.discard();
        }
        if (lifetime < 400) {
            ++lifetime;
        } else {
            this.discard();
        }
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return pPassenger == this.getOwner();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        health = health - pAmount;
        return super.hurt(pSource, pAmount);
    }
}
