package net.igneo.icv.mixin

import net.igneo.icv.enchantment.weapon.WeaponEnchantment
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.BreakthroughManager
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager
import net.igneo.icv.init.ICVUtils.getDamageFromItem
import net.igneo.icv.init.ICVUtils.getManagerForType
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.Redirect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [Player::class])
abstract class PlayerMixin protected constructor(pEntityType: EntityType<out LivingEntity?>, pLevel: Level) :
    LivingEntity(pEntityType, pLevel) {
    private var shieldHealth = 30.0
    private val checkTicks = 0

    @Shadow
    fun disableShield(pBecauseOfAxe: Boolean) {
    }

    @Redirect(
        method = ["tick"],
        at = At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V")
    )
    fun tick(instance: Player?) {
    }

    @Inject(method = ["attack"], at = [At("HEAD")], cancellable = true)
    private fun attackHead(pTarget: Entity, ci: CallbackInfo) {
        getCapability<PlayerEnchantmentActions>(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent { enchVar: PlayerEnchantmentActions ->
            for (firstEnchantment in this.mainHandItem.allEnchantments.keys) {
                val level = this.level() as ServerLevel;
                if (firstEnchantment is WeaponEnchantment && level() is ServerLevel) {
                    firstEnchantment.currentLevel = level
                    if (enchVar.getManager(4) != null && enchVar.getManager(4) is WeaponEnchantManager) {
                        val manager = enchVar.getManager(4) as WeaponEnchantManager;
                        manager.target = pTarget
                    }
                    if (enchVar.getManager(5) != null && enchVar.getManager(5) is WeaponEnchantManager) {
                        val manager = enchVar.getManager(5) as WeaponEnchantManager;
                        manager.target = pTarget
                    }
                }
            }
        }
    }

    @Inject(method = ["blockUsingShield"], at = [At("HEAD")], cancellable = true)
    protected fun blockUsingShield(pEntity: LivingEntity, ci: CallbackInfo) {
        super.blockUsingShield(pEntity)
        var damage = getDamageFromItem(pEntity.mainHandItem, pEntity)
        damage = if ((damage < 5)) 5.0 else damage
        shieldHealth -= damage

        if (pEntity is Player) {
            if (getManagerForType(pEntity, BreakthroughManager::class.java) != null) {
                shieldHealth = 0.0
            }
        }

        if (shieldHealth <= 0) {
            shieldHealth = 30.0
            this.disableShield(true)
        }
        ci.cancel()
    }
}
