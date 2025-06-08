package net.igneo.icv.mixin

import net.igneo.icv.enchantment.ICVEnchantment
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider
import net.igneo.icv.enchantmentActions.enchantManagers.trident.TridentEnchantManager
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ThrownTrident::class)
abstract class TridentEntityMixin(
    type: EntityType<out AbstractArrow>,
    level: Level
) : AbstractArrow(type, level) {

    @Shadow
    protected lateinit var tridentItem: ItemStack

    @Unique
    private val instance: ThrownTrident = this as ThrownTrident

    @Unique
    private var manager: TridentEnchantManager? = null

    @Inject(method = ["tick"], at = [At("TAIL")])
    fun tick(ci: CallbackInfo) {
        if (manager == null) {
            for (enchantment in tridentItem.allEnchantments.keys) {
                println("getting enchantments...")

                if (enchantment is ICVEnchantment && instance.owner is Player) {
                    val player = instance.owner as Player

                    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent { enchVar ->
                        println("SUCCESS! getting manager from: ${enchVar.managers.toList()}")

                        for (m in enchVar.managers) {
                            if (m is TridentEnchantManager) {
                                manager = m
                                println("OPERATION SUCCESS")
                                break
                            }
                        }

                        if (manager == null) println("FAILED")
                    }
                } else {
                    println("FAILED")
                }
            }
        }
    }

    @Inject(method = ["tryPickup"], at = [At("HEAD")], cancellable = true)
    fun tryPickup(player: Player, cir: CallbackInfoReturnable<Boolean>) {
        println("gamer")
        instance.discard()
        cir.returnValue = false
    }

    @Inject(method = ["onHitEntity"], at = [At("HEAD")], cancellable = true)
    fun onHitEntity(result: EntityHitResult, ci: CallbackInfo) {
        manager?.let {
            it.onHitEntity(result, instance)
            if (it.overrideOnHitEntity()) ci.cancel()
        }
    }

    @Unique
    override fun onHit(result: HitResult) {
        manager?.let {
            it.onHit(result, instance)
            if (!it.overrideOnHit()) {
                super.onHit(result)
            }
        } ?: super.onHit(result)
    }

    @Unique
    override fun onHitBlock(result: BlockHitResult) {
        manager?.let {
            it.onHitBlock(result, instance)
            if (!it.overrideOnHitBlock()) {
                super.onHitBlock(result)
            }
        } ?: super.onHitBlock(result)
    }
}