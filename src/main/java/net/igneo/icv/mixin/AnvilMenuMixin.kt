package net.igneo.icv.mixin

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [AnvilMenu::class])
class AnvilMenuMixin(
    pType: MenuType<*>?,
    pContainerId: Int,
    pPlayerInventory: Inventory,
    pAccess: ContainerLevelAccess
) :
    ItemCombinerMenu(pType, pContainerId, pPlayerInventory, pAccess) {
    @Shadow
    private val cost: DataSlot = DataSlot.standalone()

    @Inject(method = ["createResult"], at = [At("RETURN")])
    fun createResult(ci: CallbackInfo?) {
        if (this.inputSlots != null) {
            val checkItemStack = inputSlots.getItem(1)
            if ((checkItemStack != null && checkItemStack.isEnchanted) || checkItemStack.item === Items.ENCHANTED_BOOK) {
                resultSlots.setItem(0, ItemStack.EMPTY)
                cost.set(0)
            } else {
                inputSlots.getItem(0).setRepairCost(0)
                inputSlots.getItem(1).setRepairCost(0)
                cost.set(2)
            }
        }
    }

    @Shadow
    override fun mayPickup(pPlayer: Player, pHasStack: Boolean): Boolean {
        return false
    }

    @Shadow
    override fun onTake(pPlayer: Player, pStack: ItemStack) {
    }

    @Shadow
    override fun isValidBlock(pState: BlockState): Boolean {
        return false
    }

    @Shadow
    override fun createResult() {
    }

    @Shadow
    override fun createInputSlotDefinitions(): ItemCombinerMenuSlotDefinition? {
        return null
    }
}
