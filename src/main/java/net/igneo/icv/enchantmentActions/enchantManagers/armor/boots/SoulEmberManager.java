package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SoulEmberManager extends ArmorEnchantManager implements EntityTracker {
    public SoulEmberManager(Player player) {
        super(EnchantType.BOOTS,300,-30,false,player);
    }

    public ICVEntity child = null;

    @Override
    public void tick() {
        super.tick();
        if (child != null && !child.isAlive()) {
            child = null;
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
    public void onOffCoolDown(Player player) {

    }

    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }

    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel) {
            child = ModEntities.SOUL_EMBER.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition().subtract(0,0.7,0));
            child.setDeltaMovement(ICVUtils.getFlatDirection(player.getYRot(),2F,0));
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
