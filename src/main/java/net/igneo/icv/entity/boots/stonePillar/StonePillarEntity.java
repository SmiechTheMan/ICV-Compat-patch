package net.igneo.icv.entity.boots.stonePillar;

import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

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
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        health = health - pAmount;
        return super.hurt(pSource, pAmount);
    }
}
