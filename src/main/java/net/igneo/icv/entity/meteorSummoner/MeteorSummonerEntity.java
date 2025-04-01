package net.igneo.icv.entity.meteorSummoner;

import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MeteorSummonerEntity extends ICVEntity {
  public MeteorSummonerEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
  }
  
  private int lifetime = 0;
  private int storedLifetime = 0;
  
  @Override
  public void tick() {
    super.tick();
    if (lifetime < 300) {
      lifetime++;
      if (lifetime >= storedLifetime + 40) {
        for (Entity entity : this.level().getEntities(null, this.getBoundingBox().inflate(5))) {
          if (entity == this.getOwner() || entity instanceof ItemEntity) {
            return;
          }

          Vec3 pushDirection = entity.position().subtract(this.position()).normalize().scale(0.5);
          entity.setDeltaMovement(entity.getDeltaMovement().add(pushDirection));

          entity.hurt(this.level().damageSources().inFire(), 10);
        }
        storedLifetime = lifetime;
      }
    } else {
      this.discard();
    }
  }
}
