package net.igneo.icv.entity.helmet.divineLightningRod;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.Arrays;
import java.util.List;

public class DivineLightningRodEntity extends ICVEntity {
  private int lightningTimer;
  private int lifetime;
  private static final int LIGHTNING_INTERVAL = 200;
  private static final int MAX_LIFETIME = 600;
  private static final int LIGHTNING_DAMAGE = 15;
  private static final double LIGHTNING_RADIUS = 20.0;
  private float health = 40;
  
  private static final List<EntityType<?>> BLOCKING_ENTITIES = Arrays.asList(
    ModEntities.ICE_SPIKE.get(),
    ModEntities.STONE_PILLAR.get()
  );
  
  public DivineLightningRodEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
  }
  
  private boolean isBlockedPathToTarget(Entity target) {
    if (target == this.getOwner()) {
      return true;
    }
    
    Vec3 rodPos = this.position().add(0, 1, 0);
    Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5f, 0);
    
    BlockHitResult blockResult = this.level().clip(
      new ClipContext(
        rodPos,
        targetPos,
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        this
      )
    );
    
    if (blockResult.getType() == HitResult.Type.BLOCK) {
      return true;
    }
    
    
    AABB checkBox = new AABB(
      Math.min(rodPos.x, targetPos.x) - 1.0,
      Math.min(rodPos.y, targetPos.y) - 1.0,
      Math.min(rodPos.z, targetPos.z) - 1.0,
      Math.max(rodPos.x, targetPos.x) + 1.0,
      Math.max(rodPos.y, targetPos.y) + 1.0,
      Math.max(rodPos.z, targetPos.z) + 1.0
    );
    
    List<Entity> entitiesInPath = this.level().getEntities(this, checkBox);
    
    for (Entity entity : entitiesInPath) {
      if (entity == target || entity == this) {
        continue;
      }
      
      if (BLOCKING_ENTITIES.contains(entity.getType())) {
        AABB entityBox = entity.getBoundingBox().inflate(0.5);
        
        if (entityBox.clip(rodPos, targetPos).isPresent()) {
          return true;
        }
      }
      
      else if (entity instanceof LivingEntity) {
        AABB entityBox = entity.getBoundingBox().inflate(0.5);
        
        if (entityBox.clip(rodPos, targetPos).isPresent()) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  @Override
  public void tick() {
    super.tick();
    
    if (lifetime < MAX_LIFETIME) {
      lifetime++;
    } else {
      this.discard();
      return;
    }
    
    lightningTimer++;
    
    if (lightningTimer % LIGHTNING_INTERVAL == 0) {
      strikeLightning();
    }
  }
  
  private void strikeLightning() {
    List<Entity> nearbyEntities = this.level().getEntities(this,
      this.getBoundingBox().inflate(LIGHTNING_RADIUS));
    
    for (Entity entity : nearbyEntities) {
      if (entity == this.getOwner()) {
        continue;
      }
      
      if (isBlockedPathToTarget(entity)) {
        continue;
      }
      
      entity.hurt(this.damageSources().lightningBolt(), LIGHTNING_DAMAGE);
    }
  }
  
  @Override
  public boolean canBeCollidedWith() {
    return true;
  }
  
  @Override
  public boolean hurt(DamageSource pSource, float pAmount) {
    health -= pAmount;
    
    if (health <= 0) {
      this.discard();
    }
    
    return super.hurt(pSource, pAmount);
  }
}