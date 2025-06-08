package net.igneo.icv.mixin

import com.mojang.authlib.GameProfile
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import net.igneo.icv.ICV
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraftforge.fluids.FluidType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(value = [LocalPlayer::class], priority = 999999999)
abstract class LocalPlayerMixin(pClientLevel: ClientLevel, pGameProfile: GameProfile) :
    AbstractClientPlayer(pClientLevel, pGameProfile) {
    @Shadow
    var input: Input? = null

    @Shadow
    protected abstract fun hasEnoughFoodToStartSprinting(): Boolean

    private fun canStartSprintingmine(): Boolean {
        return this.hasEnoughImpulseToStartSprinting() && !this.isUsingItem && (!this.isPassenger || this.vehicleCanSprint(
            vehicle!!
        )) && !this.isFallFlying
    }

    private fun vehicleCanSprint(pVehicle: Entity): Boolean {
        return pVehicle.canSprint() && pVehicle.isControlledByLocalInstance
    }

    @Inject(at = [At("TAIL")], method = ["hasEnoughFoodToStartSprinting"], cancellable = true)
    fun canStartSprinting(cir: CallbackInfoReturnable<Boolean?>) {
        val animation =
            PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player!!)[ResourceLocation(
                ICV.MOD_ID,
                "enchant_animator"
            )] as ModifierLayer<IAnimation>?
        if (animation != null) {
            if (animation.isActive) cir.returnValue = false
            cir.cancel()
        }
        cir.returnValue =
            !this.isSprinting && this.hasEnoughImpulseToStartSprinting() && !this.isUsingItem && (!this.isPassenger || this.vehicleCanSprint(
                vehicle!!
            )) && !this.isFallFlying
        cir.cancel()
    }

    private fun hasEnoughImpulseToStartSprinting(): Boolean {
        val d0 = 0.8
        return if (this.isUnderWater) input!!.hasForwardImpulse() else input!!.forwardImpulse.toDouble() >= 0.8
    }

    @Inject(method = ["aiStep"], at = [At("TAIL")])
    fun aiStep(ci: CallbackInfo?) {
        if ((!(this.isInWater || this.isInFluidType { fluidType: FluidType?, height: Double? ->
                this.canSwimInFluidType(
                    fluidType
                )
            }) || (this.isUnderWater || this.canStartSwimming())) && this.hasEnoughImpulseToStartSprinting() && this.canStartSprintingmine() && !this.isUsingItem && Minecraft.getInstance().options.keySprint.isDown) {
            this.isSprinting = true
        }
        val animation =
            PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player!!)[ResourceLocation(
                ICV.MOD_ID,
                "enchant_animator"
            )] as ModifierLayer<IAnimation>?
        if (animation != null) {
            if (animation.isActive) this.isSprinting = false
        }
    }
}
