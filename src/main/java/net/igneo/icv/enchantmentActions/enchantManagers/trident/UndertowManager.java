package net.igneo.icv.enchantmentActions.enchantManagers.trident;

import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MovePlayerS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class UndertowManager extends TridentEnchantManager {
    private boolean realeased = false;
    
    public UndertowManager(Player player) {
        super(EnchantType.TRIDENT, player);
    }
    
    @Override
    public void onHitBlock(BlockHitResult result, ThrownTrident trident) {
        System.out.println("PAY ATTENTION");
        active = true;
    }
    
    @Override
    public void onHitEntity(EntityHitResult result, ThrownTrident trident) {
        System.out.println("PAY ATTENTION ENTITY");
        active = true;
    }
    
    @Override
    public void onRelease() {
        System.out.println("PAY ATTENTION RELEASE");
        realeased = true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (trident == null) {
            return;
        }
        if (!trident.onGround()) {
            return;
        }
        if (active && realeased) {
            System.out.println(active + "\n" + realeased);
        }
        pullPlayer(trident);
    }
    
    private void pullPlayer(ThrownTrident trident) {
        double xDiff = trident.getX() - player.getX();
        double yDiff = trident.getY() - player.getY();
        double zDiff = trident.getZ() - player.getZ();
        
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
        
        Vec3 playerMovement = new Vec3(
                player.getDeltaMovement().x + (xDiff / distance),
                player.getDeltaMovement().y + (yDiff / distance),
                player.getDeltaMovement().z + (zDiff / distance)
                );
        
        if (distance < 3.0d) {
            active = false;
            realeased = false;
            return;
        }
        
        player.setDeltaMovement(playerMovement);
        
        player.fallDistance = 0.0f;
        
        if (player instanceof ServerPlayer serverPlayer) {
            ModMessages.sendToPlayer(new MovePlayerS2CPacket(new Vec3(playerMovement.x, playerMovement.y, playerMovement.z)), serverPlayer);
        }
    }
}