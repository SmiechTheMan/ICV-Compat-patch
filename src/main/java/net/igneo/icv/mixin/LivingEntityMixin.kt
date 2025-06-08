package net.igneo.icv.mixin

import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.common.ToolActions
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(value = [LivingEntity::class], priority = 999999999)
abstract class LivingEntityMixin(pEntityType: EntityType<*>, pLevel: Level) :
    Entity(pEntityType, pLevel) {
    @get:Shadow
    val mainHandItem: ItemStack?
        get() = this.getItemBySlot(EquipmentSlot.MAINHAND)

    @Shadow
    fun getItemBySlot(pSlot: EquipmentSlot?): ItemStack? {
        return null
    }

    @Shadow
    abstract override fun hurt(pSource: DamageSource, pAmount: Float): Boolean

    @Shadow
    protected var useItem: ItemStack = ItemStack.EMPTY

    @Shadow
    protected var useItemRemaining: Int = 0

    @get:Shadow
    val isUsingItem: Boolean
        get() = (entityData.get(DATA_LIVING_ENTITY_FLAGS)
            .toInt() and 1) > 0

    @get:Shadow
    val isBlocking: Boolean
        get() {
            if (this.isUsingItem && !useItem.isEmpty) {
                val item = useItem.item
                return if (!useItem.canPerformAction(ToolActions.SHIELD_BLOCK)) {
                    false
                } else {
                    item.getUseDuration(this.useItem) - this.useItemRemaining >= 5
                }
            } else {
                return false
            }
        }

    /**
     * @author Igneo
     * @reason changing shield breaking
     */
    @Inject(method = ["hurt"], at = [At("HEAD")], cancellable = true)
    fun hurt(pSource: DamageSource, pAmount: Float, cir: CallbackInfoReturnable<Boolean?>?) {
        if (pSource.`is`(DamageTypes.ARROW)) {
            this.invulnerableTime = 0
        }
    }

    companion object {
        @Shadow
        protected val DATA_LIVING_ENTITY_FLAGS: EntityDataAccessor<Byte> = SynchedEntityData.defineId(
            LivingEntity::class.java, EntityDataSerializers.BYTE
        )
    }
}
