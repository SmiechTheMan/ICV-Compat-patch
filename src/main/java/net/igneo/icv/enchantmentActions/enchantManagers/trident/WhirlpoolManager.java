package net.igneo.icv.enchantmentActions.enchantManagers.trident;

import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.PushPlayerS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class WhirlpoolManager extends TridentEnchantManager {
    private boolean canPull = true;
    
    public WhirlpoolManager(Player player) {
        super(EnchantType.TRIDENT, player);
    }
    
    @Override
    public void onHitBlock(BlockHitResult result, ThrownTrident trident) {
        doPull(trident);
    }
    
    private void doPull(ThrownTrident trident) {
        if (!canPull) {
            return;
        }
        for (Entity entity : ICVUtils.collectEntitiesBox(player.level(), trident.position(), 3.5f)) {
            if (entity == trident || entity == player) {
                continue;
            }
            if (entity instanceof ServerPlayer player) {
                Vec3 pullVector = ICVUtils.DOWN_VECTOR;
                player.addDeltaMovement(pullVector);
                ModMessages.sendToPlayer(new PushPlayerS2CPacket(ICVUtils.DOWN_VECTOR), player);
            } else {
                entity.addDeltaMovement(ICVUtils.DOWN_VECTOR);
            }
        }
        canPull = false;
    }
}
