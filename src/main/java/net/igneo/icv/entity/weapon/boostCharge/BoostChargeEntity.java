package net.igneo.icv.entity.weapon.boostCharge;

import net.igneo.icv.enchantmentActions.enchantManagers.weapon.ViperManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MovePlayerS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BoostChargeEntity extends ICVEntity {
    int lifeTime = 0;
    public BoostChargeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (lifeTime > 50) {
            this.discard();
        } else {
            ++lifeTime;
        }

        if (this.onGround()) {
            for (Entity entity : ICVUtils.collectEntitiesBox(this.level(), position(),3)) {
                entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x,0.7,entity.getDeltaMovement().x));
                if (entity instanceof ServerPlayer player) {
                    ModMessages.sendToPlayer(new MovePlayerS2CPacket(new Vec3(0,0.7,0)),player);
                }
            }
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        this.discard();
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (pResult instanceof EntityHitResult hitResult) {
            if (hitResult.getEntity() == this.getOwner()) return;
        }
        for (Entity entity : ICVUtils.collectEntitiesBox(this.level(), position(),3)) {
            entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x,0.7,entity.getDeltaMovement().x));
            if (entity instanceof ServerPlayer player) {
                ModMessages.sendToPlayer(new MovePlayerS2CPacket(new Vec3(0,0.7,0)),player);
            }
        }
        this.discard();
    }
}
