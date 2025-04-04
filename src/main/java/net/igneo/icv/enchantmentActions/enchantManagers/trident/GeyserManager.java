package net.igneo.icv.enchantmentActions.enchantManagers.trident;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MovePlayerS2CPacket;
import net.igneo.icv.networking.packet.PushPlayerS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GeyserManager extends TridentEnchantManager {
    public GeyserManager(Player player) {
        super(EnchantType.TRIDENT, player);
    }

    private boolean canPush = true;

    @Override
    public void onHitBlock(BlockHitResult result, ThrownTrident trident) {
        System.out.println("no, no, its working");
        doPush(trident);
    }
    @Override
    public void onHitEntity(EntityHitResult result, ThrownTrident trident) {
        System.out.println("no, no, its working");
        if (result.getEntity().onGround()) {
            doPush(trident);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (trident == null) {
            canPush = true;
        } else if (trident.onGround() || trident.getDeltaMovement().length() < 0.1) {
            doPush(trident);
        }
    }

    public void doPush(ThrownTrident trident) {
        if (canPush) {
            for (Entity entity : ICVUtils.collectEntitiesBox(player.level(), trident.position(), 3.5)) {
                if (entity != trident) {
                    if (entity instanceof ServerPlayer player) {
                        System.out.println("pushing the player");
                        player.addDeltaMovement(new Vec3(0,1,0));
                        ModMessages.sendToPlayer(new PushPlayerS2CPacket(new Vec3(0,1,0)),player);
                    } else {
                        entity.addDeltaMovement(new Vec3(0, 1, 0));
                    }
                }
            }
            canPush = false;
        }
    }
}
