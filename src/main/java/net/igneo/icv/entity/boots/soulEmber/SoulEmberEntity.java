package net.igneo.icv.entity.boots.soulEmber;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SoulEmberEntity extends ICVEntity {
    public SoulEmberEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOwner() != null) {
            Vec3 baseVec = this.getOwner().getEyePosition().subtract(this.position()).normalize();
            this.addDeltaMovement(new Vec3(baseVec.x, 0, baseVec.z).scale(0.22));

        } else {
            discard();
        }
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return pPassenger == this.getOwner();
    }

    @Override
    public double getAirFriction() {
        return 0.2;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity().equals(this.getOwner())) {
            this.discard();
        } else if (pResult.getEntity() instanceof LivingEntity entity){
            entity.hurt(this.damageSources().inFire(),5);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() != null) {
            this.setDeltaMovement(ICVUtils.getFlatDirection(pSource.getEntity().getYRot(),3F,0));
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
