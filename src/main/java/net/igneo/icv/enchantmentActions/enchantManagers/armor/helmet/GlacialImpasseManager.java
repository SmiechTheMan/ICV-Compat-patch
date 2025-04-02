package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GlacialImpasseManager extends ArmorEnchantManager implements EntityTracker {
  
  private ICVEntity spawner = null;
  private ICVEntity segment = null;
  private Vec3 spawnerPosition;
  private int tickCount = 0;
  
  public GlacialImpasseManager(Player player) {
    super(EnchantType.HELMET, 300, -10, false, player);
  }
  
  @Override
  public void activate() {
    Vec3 direction = player.getViewVector(1.0F).normalize();
    spawnerPosition = player.position().add(direction.scale(2.5));
    
    if (player.level() instanceof ServerLevel level) {
      spawner = ModEntities.ICE_SPIKE_SPAWNER.get().create(level);
      if (spawner != null) {
        spawner.setOwner(player);
        spawner.setPos(spawnerPosition);
        
        level.addFreshEntity(spawner);
        syncClientChild((ServerPlayer) player, spawner, this);
      }
    }
    active = true;
  }
  
  @Override
  public void tick() {
    super.tick();
    if (!active) {
      return;
    }
    
    tickCount++;
    
    if (tickCount >= 200 && spawner != null) {
      spawner.discard();
      spawner = null;
    }
    
    if (tickCount >= 400 && segment != null) {
      segment.discard();
      segment = null;
    }
    
    if (spawner != null) {
      spawnerPosition = spawner.position();
      spawnSegment(spawnerPosition);
    }
    
    if (segment != null && !segment.isAlive()) {
      segment = null;
    }
    if (spawner != null && !spawner.isAlive()) {
      spawner = null;
    }
  }
  
  private void spawnSegment(Vec3 position) {
    if (player.level() instanceof ServerLevel level) {
      segment = ModEntities.ICE_SPIKE.get().create(level);
      if (segment != null) {
        segment.setOwner(player);
        segment.setPos(position);
        
        level.addFreshEntity(segment);
        syncClientChild((ServerPlayer) player, segment, this);
      }
    }
  }
  
  @Override
  public boolean canUse() {
    return segment == null && spawner == null;
  }
  
  @Override
  public boolean shouldTickCooldown() {
    return segment == null && spawner == null;
  }
  
  @Override
  public void onRemove() {
    if (spawner != null) {
      spawner.discard();
      spawner = null;
    }
    if (segment != null) {
      segment.discard();
      segment = null;
    }
  }
  
  @Override
  public void onOffCoolDown(Player player) {
  
  }
  
  @Override
  public EnchantIndicator getIndicator() {
    return new StasisCooldownIndicator(this);
  }
  
  @Override
  public ICVEntity getChild() {
    return segment;
  }
  
  @Override
  public void setChild(ICVEntity entity) {
    segment = entity;
  }
}