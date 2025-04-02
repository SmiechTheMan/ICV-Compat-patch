package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlacialImpasseManager extends ArmorEnchantManager implements EntityTracker {
  
  private ICVEntity spawner = null;
  private final List<ICVEntity> segments = new ArrayList<>();
  private final Map<ICVEntity, Vec3> originalPositions = new HashMap<>();
  private int tickCount = 0;
  private static final double PROXIMITY_THRESHOLD = 3.0d;
  private static final double MOVEMENT_DISTANCE = 1.5d;
  
  public GlacialImpasseManager(Player player) {
    super(EnchantType.HELMET, 300, -10, false, player);
    this.player = player;
    this.active = false;
  }
  
  @Override
  public void activate() {
    tickCount = 0;
    Vec3 direction = player.getViewVector(1.0F).normalize();
    Vec3 spawnerPosition = player.position().add(direction.scale(2.5));
    
    BlockPos blockPos = new BlockPos((int)spawnerPosition.x, (int)spawnerPosition.y, (int)spawnerPosition.z);
    if (player.level().getBlockState(blockPos).isCollisionShapeFullBlock(player.level(), blockPos)) {
      return;
    }
    
    if (!(player.level() instanceof ServerLevel level)) {
      return;
    }
    
    spawner = ModEntities.ICE_SPIKE_SPAWNER.get().create(level);
    if (spawner == null) {
      return;
    }
    
    spawner.setOwner(player);
    spawner.setPos(spawnerPosition);
    level.addFreshEntity(spawner);
    syncClientChild((ServerPlayer) player, spawner, this);
    active = true;
  }
  
  @Override
  public void tick() {
    super.tick();
    
    if (!active) {
      return;
    }
    
    tickCount++;
    handleTimeouts();
    handleSpawnerPositioning();
    updateSegmentPositions();
    segments.removeIf(segment -> !segment.isAlive());
    
    if (segments.isEmpty()) {
      active = false;
    }
  }
  
  private void updateSegmentPositions() {
    for (ICVEntity segment : segments) {
      if (!originalPositions.containsKey(segment)) {
        continue;
      }
      
      double distanceToPlayer = segment.position().distanceTo(player.position());
      Vec3 originalPos = originalPositions.get(segment);
      
      if (distanceToPlayer <= PROXIMITY_THRESHOLD) {
        Vec3 loweredPos = new Vec3(originalPos.x, originalPos.y - MOVEMENT_DISTANCE, originalPos.z);
        segment.setPos(loweredPos);
      } else {
        segment.setPos(originalPos);
      }
    }
  }
  
  private void handleTimeouts() {
    if (tickCount >= 200 && spawner != null) {
      spawner.discard();
      spawner = null;
    }
    
    if (tickCount >= 400) {
      segments.forEach(ICVEntity::discard);
      segments.clear();
      originalPositions.clear();
      active = false;
    }
  }
  
  private void handleSpawnerPositioning() {
    if (spawner == null) {
      return;
    }
    
    Vec3 spawnerPos = spawner.position();
    if (isInsideBlock(spawnerPos)) {
      cleanupEntities();
      return;
    }
    
    BlockHitResult hit = player.level().clip(
      new ClipContext(
        spawnerPos,
        spawnerPos.add(0, -1, 0),
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        player
      )
    );
    
    if (hit.getType() != HitResult.Type.BLOCK) {
      return;
    }
    
    if (hit.getBlockPos().getY() <= spawnerPos.y) {
      handleSpawnerMovement(spawnerPos);
      return;
    }
    
    active = false;
  }
  
  private void handleSpawnerMovement(Vec3 spawnerPos) {
    Vec3 newPos = spawnerPos.add(0, 1, 0);
    if (isInsideBlock(newPos)) {
      cleanupEntities();
      return;
    }
    
    spawner.setPos(newPos);
    spawnSegment(spawner.position());
  }
  
  private void cleanupEntities() {
    if (spawner != null) {
      spawner.discard();
      spawner = null;
    }
    
    segments.forEach(ICVEntity::discard);
    segments.clear();
    originalPositions.clear();
    active = false;
  }
  
  private boolean isInsideBlock(Vec3 position) {
    if (!(player.level() instanceof ServerLevel level)) {
      return false;
    }
    
    BlockPos blockPos = new BlockPos((int)position.x, (int)position.y, (int)position.z);
    BlockState blockState = level.getBlockState(blockPos);
    
    return blockState.isCollisionShapeFullBlock(level, blockPos);
  }
  
  private void spawnSegment(Vec3 position) {
    if (!(player.level() instanceof ServerLevel level)) {
      return;
    }
    
    if (isInsideBlock(position)) {
      return;
    }
    
    for (ICVEntity existingSegment : segments) {
      if (existingSegment.position().distanceTo(position) < 1.5) {
        return;
      }
    }
    
    ICVEntity segment = ModEntities.ICE_SPIKE.get().create(level);
    if (segment == null) {
      return;
    }
    
    segment.setOwner(player);
    segment.setPos(position);
    level.addFreshEntity(segment);
    syncClientChild((ServerPlayer) player, segment, this);
    segments.add(segment);
    originalPositions.put(segment, position); // Store the original position
  }
  
  @Override
  public boolean canUse() {
    return segments.isEmpty();
  }
  
  @Override
  public boolean shouldTickCooldown() {
    return segments.isEmpty();
  }
  
  @Override
  public void onRemove() {
    cleanupEntities();
  }
  
  @Override
  public void onOffCoolDown(Player player) {
    // Empty implementation
  }
  
  @Override
  public EnchantIndicator getIndicator() {
    return new StasisCooldownIndicator(this);
  }
  
  @Override
  public ICVEntity getChild() {
    return segments.isEmpty() ? null : segments.get(0);
  }
  
  @Override
  public void setChild(ICVEntity entity) {
    segments.clear();
    originalPositions.clear();
    segments.add(entity);
    originalPositions.put(entity, entity.position());
  }
}