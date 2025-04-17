package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class KineticManager extends WeaponEnchantManager {
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("f72bbef0-6198-4994-96f4-b975a86e2085");
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("a8b4e7d1-9c65-4e73-8f12-d09a8c621b37");
    private int kineticChargeCount = 0;
    private int kineticTimer = 0;
    private boolean damageBoostActive = false;
    
    public KineticManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "kinetic"));
    }
    
    @Override
    public void onAttack(Entity target) {
        if (player != null) {
            kineticChargeCount++;
            kineticTimer = 0;
            
            double speedBonus = 0.25d * kineticChargeCount;
            player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED)
                    .addTransientModifier(new AttributeModifier(SPEED_MODIFIER_UUID,
                            "Kinetic Speed Boost",
                            speedBonus,
                            AttributeModifier.Operation.ADDITION
                    ));
        }
        
        if (damageBoostActive) {
            damageBoostActive = false;
        }
    }
    
    @Override
    public void activate() {
        if (player != null && kineticChargeCount > 0) {
            double bonusDamage = kineticChargeCount * 1.5d;
            player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_UUID);
            player.getAttributes()
                    .getInstance(Attributes.ATTACK_DAMAGE)
                    .addTransientModifier(new AttributeModifier(
                            DAMAGE_MODIFIER_UUID,
                            "Kinetic Damage Boost",
                            bonusDamage,
                            AttributeModifier.Operation.ADDITION
                    ));
            
            kineticChargeCount = 0;
            damageBoostActive = true;
        }
    }
    
    @Override
    public void tick() {
        if (kineticChargeCount < 0) {
            return;
        }
        kineticTimer++;
        if (kineticTimer <= 180) {
            return;
        }
        resetKineticEffects();
    }
    
    private void resetKineticEffects() {
        kineticChargeCount = 0;
        kineticTimer = 0;
        damageBoostActive = false;
        player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_UUID);
        player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_MODIFIER_UUID);
    }
    
    @Override
    public boolean canUse() {
        return true;
    }
}
