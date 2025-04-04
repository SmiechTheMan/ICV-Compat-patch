package net.igneo.icv.enchantmentActions.enchantManagers.trident;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.igneo.icv.ICV;
import net.igneo.icv.client.animation.EnchantAnimationPlayer;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class TridentEnchantManager extends EnchantmentManager {

    public ThrownTrident trident;
    public ItemStack tridentItem;


    protected TridentEnchantManager(EnchantType setSlot, Player player) {
        super(setSlot, player);

        int slot = ICVUtils.getSlotForType(player,this.getClass());
        this.tridentItem = ICVUtils.getItemForSlot(player,slot);
    }

    public void onHit(HitResult result, ThrownTrident trident) {}
    public boolean overrideOnHit() {
        return false;
    }
    public void onHitEntity(EntityHitResult result, ThrownTrident trident) {}
    public boolean overrideOnHitEntity() {
        return false;
    }
    public void onHitBlock(BlockHitResult result, ThrownTrident trident) {}
    public boolean overrideOnHitBlock() {
        return false;
    }

    @Override
    public boolean canUse() {
        return tridentThrown();
    }

    private boolean tridentThrown() {
        if (tridentItem != null && tridentItem.hasTag()) {
            ThrownTrident trident = (ThrownTrident) Minecraft.getInstance().level.getEntity(tridentItem.getTag().getInt("tridentID"));
            if (!(trident == null || !trident.isAddedToWorld())) {
                this.trident = trident;
                return true;
            }
        }
        trident = null;
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void use() {
        if (this.canUse()) {
            activate();
        }
    }

    @Override
    public void onEquip() {
        // this will play a sound later
    }

    @Override
    public void activate() {

    }

    public float getDamageBonus() {
        return 0;
    }

    public void onRelease() {
    }
}
