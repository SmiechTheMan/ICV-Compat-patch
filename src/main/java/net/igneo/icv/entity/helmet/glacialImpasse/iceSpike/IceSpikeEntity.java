package net.igneo.icv.entity.helmet.glacialImpasse.iceSpike;

import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class IceSpikeEntity extends ICVEntity {
    private int lifetime;
    
    public IceSpikeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public float getStepHeight() {
        return 1.5f;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (lifetime < 400) {
            lifetime++;
        } else {
            this.discard();
        }
        
        hurtPlayer();
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    public void hurtPlayer() {
        for (Entity entity : this.level().getEntities(null, this.getBoundingBox().inflate(1.01d))) {
            if (entity == this.getOwner()) {
                return;
            }
            entity.hurt(this.damageSources().freeze(), 3);
        }
    }
}
