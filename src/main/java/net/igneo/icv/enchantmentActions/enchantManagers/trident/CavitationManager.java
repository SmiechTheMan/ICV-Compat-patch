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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CavitationManager extends TridentEnchantManager {
    public CavitationManager(Player player) {
        super(EnchantType.TRIDENT, player);
    }
    
    @Override
    public void onHitBlock(BlockHitResult result, ThrownTrident trident) {
        pullTowards(trident);
    }
    
    @Override
    public void onHitEntity(EntityHitResult result, ThrownTrident trident) {
        pullTowards(trident);
    }
    
    private void pullTowards(ThrownTrident trident) {
        List<Entity> nearybyEntities = ICVUtils.collectEntitiesBox(player.level(), trident.position(), 3.5f);
        
        for (Entity entity : nearybyEntities) {
            if (entity == player) {
                continue;
            }
            Vec3 direction = trident.position().subtract(entity.position()).normalize();
            
            Vec3 pullVeloctiy = direction.scale(1.5f);
            entity.addDeltaMovement(pullVeloctiy);
            entity.hurtMarked = true;
            
            if (entity instanceof ServerPlayer) {
                ModMessages.sendToPlayer(new PushPlayerS2CPacket(pullVeloctiy), (ServerPlayer) player);
            }
        }
    }
}
