package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.weapon.FireRing.FireRingEntity;
import net.igneo.icv.entity.weapon.ember.EmberEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

public class MoltenManager extends WeaponEnchantManager{

    public MoltenManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
    }

    @Override
    public float getDamageBonus() {
        if(target.isOnFire()){
            target.extinguishFire();
            if (target.level() instanceof ServerLevel level) {
                level.playSound(null, target.blockPosition(), ModSounds.PARRY.get(), SoundSource.PLAYERS);
                for(int i = 0; i <= 2; i++){
                    EmberEntity child = ModEntities.EMBER.get().create(player.level());
                    child.setOwner(player);
                    child.setPos(target.getEyePosition().subtract(0,1,0));
                    child.setDeltaMovement(ICVUtils.getFlatDirection((float) (Math.random()*360),0.2f,0.3f));
                    level.addFreshEntity(child);
                }
                return 3.0f;
            }
        }

        return super.getDamageBonus();
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void activate() {
        super.activate();
        FireRingEntity child = ModEntities.FIRE_RING.get().create(player.level());
        child.setOwner(player);
        child.setPos(player.position());
        //child.setDeltaMovement(ICVUtils.getFlatDirection((float) (Math.random()*360),0.2f,0.3f));
        player.level().addFreshEntity(child);
    }


}
