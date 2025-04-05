package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.igneo.icv.ICV;
import net.igneo.icv.client.animation.EnchantAnimationPlayer;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class JudgementManager extends ArmorEnchantManager {
    private boolean kicking = false;
    private int kickTicks = 0;
    private Vec3 kickPos;
    private Entity judged;
    public JudgementManager(Player player) {
        super(EnchantType.LEGGINGS, 100, -10, false, player);
    }

    @Override
    public void activate() {
        active = true;
        System.out.println("activating");
        if (player.level() instanceof ClientLevel) {
            this.animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_jump"))));
        } else {
        }
        kickPos = this.player.position();
    }

    @Override
    public void onOffCoolDown(Player player) {

    }

    @Override
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (active) {
            if (kicking && !this.player.onGround()) {
                if (kickTicks < 10) {
                    ++kickTicks;
                    player.setPos(kickPos);
                    if (kickTicks == 10) {
                        double yaw = Math.toRadians(player.getYRot());
                        double x = -Math.sin(yaw);
                        double y = 0.5;
                        double z = Math.cos(yaw);
                        double scale = 2;

                        Vec3 flatDirection = new Vec3(x*scale, y, z*scale);
                        judged.setDeltaMovement(flatDirection);
                        scale = -0.5;
                        flatDirection = new Vec3(x*scale, y, z*scale);
                        player.setDeltaMovement(flatDirection);
                    }
                }
            } else if (activeTicks > 24){
                kickPos = this.player.position();
                for (Entity entity : player.level().getEntities(null,player.getBoundingBox().inflate(1.2))) {
                    if (entity != player) {
                        kicking = true;
                        judged = entity;
                        if (player.level().isClientSide) {
                            animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_hit"))));
                        } else {
                        }
                    }
                }
            }
            if (activeTicks == 24) {
                double yaw = Math.toRadians(player.getYRot());
                double x = -Math.sin(yaw);
                double y = 0.35;
                double z = Math.cos(yaw);
                double scale = 3.5;

                Vec3 flatDirection = new Vec3(x*scale, y, z*scale);
                player.setDeltaMovement(flatDirection);
            } else if (activeTicks < 24) {
                if (kickPos != null) player.setPos(kickPos);
            }
            if (player.level().isClientSide) {
                if (!animator.isActive()) {
                    animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_idle"))));
                }
            }
            if (player.onGround() && activeTicks > 28 && !(kicking && kickTicks < 10)) {
                if (!kicking) {
                    if (player.level().isClientSide) {
                        animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_miss"))));
                    }
                    double yaw = Math.toRadians(player.getYRot());
                    double x = -Math.sin(yaw);
                    double y = 0;
                    double z = Math.cos(yaw);
                    double scale = 2.5;

                    Vec3 flatDirection = new Vec3(x*scale, y, z*scale);
                    player.setDeltaMovement(flatDirection);
                }
                activeTicks = 0;
                active = false;
                kicking = false;
                kickPos = null;
                kickTicks = 0;
            }
        }
    }
}
