package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SurfManager extends ArmorEnchantManager implements EntityTracker {
    public SurfManager(Player player) {
        super(EnchantType.BOOTS,300,-30,true,player);
    }

    public ICVEntity child = null;
    private boolean riding;

    @Override
    public void tick() {
        super.tick();
        if (child != null) {
            if (!child.isAlive()) {
                child = null;
            } else if (!riding){
                player.startRiding(child);
                riding = true;
            }
        }
    }

    @Override
    public boolean canUse() {
        return child == null;
    }

    @Override
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }

    @Override
    public void resetCoolDown() {
        super.resetCoolDown();
        riding = false;
    }

    @Override
    public void onOffCoolDown(Player player) {

    }

    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }

    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel) {
            child = ModEntities.SURF_WAVE.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition());
            player.level().addFreshEntity(child);
            syncClientChild((ServerPlayer) player,child,this);
        }
    }

    @Override
    public void onRemove() {
        if (this.child != null) this.child.discard();
    }

    @Override
    public ICVEntity getChild() {
        return child;
    }

    @Override
    public void setChild(ICVEntity entity) {
        child = entity;
    }
}
