package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.stonePillar.StonePillarEntity;
import net.igneo.icv.entity.voidSpike.VoidSpikeEntity;
import net.igneo.icv.entity.voidSpike.VoidSpikeModel;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class VoidWakeManager extends ArmorEnchantManager {
    public VoidWakeManager(Player player) {
        super(EnchantType.LEGGINGS, 300, -10, true, player);
    }
    private int storedtick = 0;
    private boolean removing;
    private ArrayList<VoidSpikeEntity> spikes = new ArrayList<>();
    @Override
    public void activate() {
        System.out.println("activating on the " + player.level());
        active = true;
    }

    @Override
    public void onOffCoolDown(Player player) {

    }

    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (active) {
            if (activeTicks > storedtick + (removing ? 10 : 20) && player.level() instanceof ServerLevel level) {
                if (!removing) {
                    VoidSpikeEntity entity = ModEntities.VOID_SPIKE.get().create(level);
                    entity.setPos(player.position());
                    entity.setOwner(player);
                    level.addFreshEntity(entity);
                    spikes.add(entity);
                    storedtick = activeTicks;
                } else {
                    System.out.println("removing variable");
                    if (!spikes.isEmpty()) {
                        spikes.get(spikes.size()-1).discard();
                        storedtick = activeTicks;
                    } else {
                        active = false;
                        resetCoolDown();
                    }
                }
            }

            if (activeTicks > 300) {
                removing = true;
            }
        }
    }

    @Override
    public void dualActivate() {
        super.dualActivate();
        if (player.level() instanceof ServerLevel level) {
            HitResult hitResult = player.pick(30, 0f, false);
            Vec3 position = player.position().add(player.getLookAngle().scale(2));
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                position = blockHitResult.getLocation();
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                position = entityHitResult.getEntity().position();
            }
            for (VoidSpikeEntity entity : spikes) {
                System.out.println(hitResult.getType());
                System.out.println(position);
                Vec3 pushVec = position.subtract(entity.position()).normalize().scale(3);
                entity.setDeltaMovement(new Vec3(pushVec.x,position.y - entity.position().y + 0.3,pushVec.z));
                entity.launched = true;
            }
        }
        active = false;
        resetCoolDown();
    }

    @Override
    public boolean canUse() {
        return !active;
    }

    @Override
    public void resetCoolDown() {
        super.resetCoolDown();
        spikes.clear();
        storedtick = 0;
        activeTicks = 0;
        removing = false;
    }
}


