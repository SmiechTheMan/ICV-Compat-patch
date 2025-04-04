package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.boots.stonePillar.StonePillarEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;

public class StoneCallerManager extends ArmorEnchantManager {
    public StoneCallerManager(Player player) {
        super(EnchantType.BOOTS, 300, -10, false, player);
    }
    public Vec3 position = Vec3.ZERO;
    @Override
    public void activate() {

        if (player.level() instanceof ServerLevel level) {
            HitResult hitResult = player.pick(20, 0f, false);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                position = blockHitResult.getLocation();
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                position = entityHitResult.getEntity().position();
            }

            for (int i = 8; i > 0; --i) {
                float rot = Input.getRotation(Input.getInput(i));
                StonePillarEntity entity = ModEntities.STONE_PILLAR.get().create(level);
                entity.setPos(position.add(ICVUtils.getFlatDirection(rot,3,0)));
                level.addFreshEntity(entity);
            }
        }
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
            if (activeTicks >= 1200) {
                activeTicks = 0;
                active = false;
            } else {
                ++activeTicks;
            }
        }
    }

    @Override
    public boolean canUse() {
        return !active;
    }
}


