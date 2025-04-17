package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class MeathookManager extends WeaponEnchantManager {
    public MeathookManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
    }
    
    private final HashMap<Entity, Integer> meat = new HashMap<>();
    
    @Override
    public void onAttack(Entity entity) {
        super.onAttack(entity);
        meat.put(entity, 200);
    }
    
    @Override
    public void tick() {
        super.tick();
        for (Entity entity : meat.keySet()) {
            meat.replace(entity, meat.get(entity) - 1);
            if (meat.get(entity) <= 0) {
                meat.remove(entity);
            }
        }
    }
    
    @Override
    public void activate() {
        super.activate();
        double x = 0;
        double y = 0;
        double z = 0;
        for (Entity entity : meat.keySet()) {
            x += entity.getX();
            y += entity.getY();
            z += entity.getZ();
        }
        x = x / meat.keySet().size();
        y = y / meat.keySet().size();
        z = z / meat.keySet().size();
        Vec3 average = new Vec3(x, y, z);
        
        for (Entity entity : meat.keySet()) {
            if (entity.distanceToSqr(average) < 70) {
                Vec3 pushVec = entity.position().subtract(average).normalize().reverse();
                entity.addDeltaMovement((entity.onGround() ? new Vec3(pushVec.x, 0.5, pushVec.z) : pushVec));
            }
        }
        
        meat.clear();
    }
}
