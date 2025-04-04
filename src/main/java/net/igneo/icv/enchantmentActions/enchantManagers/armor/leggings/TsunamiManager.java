package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.leggings.wave.WaveEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class TsunamiManager extends ArmorEnchantManager {
    public TsunamiManager(Player player) {
        super(EnchantType.LEGGINGS, 300, -10, false, player);
    }
    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel level) {
            for (int i = 8; i > 0; --i) {
                float rot = Input.getRotation(Input.getInput(i));
                WaveEntity entity = ModEntities.WAVE.get().create(level);
                entity.setPos(player.position().add(ICVUtils.getFlatDirection(rot,2,0)));
                entity.setTrajectory(ICVUtils.getFlatDirection(rot,1,0));
                level.addFreshEntity(entity);
            }

        }
        resetCoolDown();
    }

    @Override
    public void onOffCoolDown(Player player) {

    }

    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
}


