package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import com.alrex.parcool.common.action.impl.Dodge;
import com.alrex.parcool.common.action.impl.Slide;
import com.alrex.parcool.common.capability.Parkourability;
import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.weapon.boostCharge.BoostChargeEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VolatileManager extends WeaponEnchantManager{
    public VolatileManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
    }

    @Override
    public void onAttack(Entity target) {
        if (player.fallDistance > 0) {
            for (Entity entity : ICVUtils.collectEntitiesBox(target.level(),target.position(),3)) {
                if (entity != player && entity != target) {
                    entity.hurt(player.damageSources().explosion(player,entity),7);
                }
            }
        }
    }

    @Override
    public void activate() {
        super.activate();
        if (player.level() instanceof ServerLevel level) {
            BoostChargeEntity child = ModEntities.BOOST_CHARGE.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition().subtract(0,1,0));
            child.setDeltaMovement(player.getLookAngle().scale(0.6));
            level.addFreshEntity(child);
        }
    }

    @Override
    public boolean stableCheck() {
        return !enchVar.animated &&
                !player.isSwimming() &&
                !Parkourability.get(player).get(Dodge.class).isDoing() &&
                !Parkourability.get(player).get(Slide.class).isDoing();
    }
}
