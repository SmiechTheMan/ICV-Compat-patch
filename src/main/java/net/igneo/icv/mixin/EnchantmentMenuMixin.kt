package net.igneo.icv.mixin

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.util.RandomSource
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EnchantmentTableBlock
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Unique

@Mixin(value = [EnchantmentMenu::class])
class EnchantmentMenuMixin //super(MenuType.ENCHANTMENT, pContainerId);
@OnlyIn(Dist.CLIENT) protected constructor(
    pContainerId: Int,
    pPlayerInventory: Inventory?,
    @field:Shadow private val access: ContainerLevelAccess
) :
    AbstractContainerMenu(MenuType.ENCHANTMENT, pContainerId) {
    @Shadow
    private val random: RandomSource = RandomSource.create()

    @Shadow
    private val enchantmentSeed: DataSlot = DataSlot.standalone()

    @Shadow
    val enchantClue: IntArray = intArrayOf(-1, -1, -1)

    @Shadow
    val levelClue: IntArray = intArrayOf(-1, -1, -1)

    private var localEnchShift = 0
    private var localLength = 0
    private var player: ServerPlayer? = null

    @Shadow
    private val enchantSlots: Container = object : SimpleContainer(2) {
        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        override fun setChanged() {
            super.setChanged()
            this@EnchantmentMenuMixin.slotsChanged(this)
        }
    }

    @Shadow
    var costs: IntArray = IntArray(3)

    @Shadow
    override fun quickMoveStack(pPlayer: Player, pIndex: Int): ItemStack? {
        return null
    }

    @Shadow
    override fun stillValid(pPlayer: Player): Boolean {
        return false
    }

    /**
     * @author Igneo220
     * @reason Rewriting enchantment table
     */
    @Overwrite
    override fun clickMenuButton(pPlayer: Player, pId: Int): Boolean {
        if (pId >= 0) {
            val itemstack = enchantSlots.getItem(0)
            val itemstack1 = enchantSlots.getItem(1)
            //int i = pId + 1;
            if ((itemstack1.isEmpty && !pPlayer.isCreative) || (itemstack1.count < 1 && !pPlayer.isCreative)) {
                return false
            } else if ((itemstack.isEmpty && !pPlayer.isCreative) || (pPlayer.experienceLevel < 1 && !pPlayer.isCreative)) {
                return false
            } else {
                access.execute { level: Level, tablePos: BlockPos ->
                    var itemstack2 = itemstack
                    val list = this.getChiselEnchantmentList(level, tablePos, itemstack)
                    if (!list.isEmpty()) {
                        pPlayer.onEnchantmentPerformed(itemstack, 1)
                        val flag = itemstack.`is`(Items.BOOK)
                        if (flag) {
                            itemstack2 = ItemStack(Items.ENCHANTED_BOOK)
                            val compoundtag = itemstack.tag
                            if (compoundtag != null) {
                                itemstack2.tag = compoundtag.copy()
                            }

                            enchantSlots.setItem(0, itemstack2)
                        }

                        for (j in list.indices) {
                            val enchantmentinstance = list[pId]
                            if (flag) {
                                EnchantedBookItem.addEnchantment(itemstack2, enchantmentinstance)
                            } else {
                                itemstack2.enchant(enchantmentinstance.enchantment, 1)
                            }
                        }

                        if (!pPlayer.abilities.instabuild) {
                            itemstack1.shrink(1)
                            if (itemstack1.isEmpty) {
                                enchantSlots.setItem(1, ItemStack.EMPTY)
                            }
                        }

                        pPlayer.awardStat(Stats.ENCHANT_ITEM)
                        if (pPlayer is ServerPlayer) {
                            CriteriaTriggers.ENCHANTED_ITEM.trigger(pPlayer, itemstack2, 1)
                        }

                        enchantSlots.setChanged()
                        enchantmentSeed.set(pPlayer.enchantmentSeed)
                        this.slotsChanged(this.enchantSlots)
                        level.playSound(
                            null,
                            tablePos,
                            SoundEvents.ENCHANTMENT_TABLE_USE,
                            SoundSource.BLOCKS,
                            1.0f,
                            level.random.nextFloat() * 0.1f + 0.9f
                        )
                    }
                }
                return true
            }
        } else {
            if (this.localLength > 3) {
                if (pId == -1) {
                    if (this.localEnchShift + 3 < this.localLength) {
                        ++this.localEnchShift
                    }
                } else if (pId == -2 && this.localEnchShift > 0) {
                    --this.localEnchShift
                }
            } else {
                this.localEnchShift = 0
            }
            if (pPlayer is ServerPlayer) {
                //ModMessages.sendToPlayer(new EnchTableUpdateS2CPacket(this.localEnchShift), (ServerPlayer) pPlayer);
                this.player = pPlayer
            }
            slotsChanged(enchantSlots)
            return false
        }
    }

    /**
     * @author Igneo220
     * @reason Rewriting enchantment system
     */
    @Overwrite
    override fun slotsChanged(pInventory: Container) {
        if (this.player != null) {
            //ModMessages.sendToPlayer(new EnchTableUpdateS2CPacket(this.localEnchShift), (ServerPlayer) player);
        }
        updateEnchantmentLists()
        if (pInventory === this.enchantSlots) {
            val itemstack = pInventory.getItem(0)
            if (!itemstack.isEmpty && itemstack.isEnchantable && checkValid(itemstack)) {
                access.execute { level: Level, tablePos: BlockPos ->
                    val j = 0f
                    for (k in 0..2) {
                        costs[k] = 1
                    }

                    for (l in 0..2) {
                        val list =
                            getChiselEnchantmentList(level, tablePos, itemstack)
                        if (list != null && !list.isEmpty()) {
                            if (l < list.size) {
                                val enchantmentinstance = list[l + this.localEnchShift]
                                enchantClue[l] =
                                    BuiltInRegistries.ENCHANTMENT.getId(enchantmentinstance.enchantment)
                                levelClue[l] = enchantmentinstance.level
                            } else {
                                enchantClue[l] = -1
                            }
                        } else {
                            for (i in 0..2) {
                                costs[i] = 0
                                enchantClue[i] = -1
                                levelClue[i] = -1
                            }
                            this.localEnchShift = 0
                            this.localLength = 0
                        }
                    }
                    this.broadcastChanges()
                }
            } else {
                for (i in 0..2) {
                    costs[i] = 0
                    enchantClue[i] = -1
                    levelClue[i] = -1
                }
                this.localEnchShift = 0
                this.localLength = 0
            }
        }
    }

    private fun checkValid(pStack: ItemStack): Boolean {
        var valid = false
        if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.MAINHAND) {
            if (pStack.item == Items.BOW) {
                valid = true
            } else if (pStack.item == Items.CROSSBOW) {
                valid = true
            } else if (pStack.item == Items.TRIDENT) {
                valid = true
            } else if (pStack.item.toString().contains("sword") ||
                pStack.item.toString().contains("scythe") ||
                pStack.item.toString().contains("glaive") ||
                pStack.item.toString().contains("halberd") ||
                pStack.item.toString().contains("hammer") ||
                pStack.item.toString().contains("rapier") ||
                pStack.item.toString().contains("spear") ||
                pStack.item.toString().contains("katana") ||
                pStack.item.toString().contains("mace")
            ) {
                valid = true
            } else if (pStack.item.toString().contains("axe") ||
                pStack.item.toString().contains("hoe") ||
                pStack.item.toString().contains("shovel")
            ) {
                valid = true
            }
        } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.HEAD) {
            valid = true
        } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.CHEST) {
            valid = true
        } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.LEGS) {
            valid = true
        } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.FEET) {
            valid = true
        }
        return valid
    }

    /**
     * @author Igneo220
     * @reason rewriting enchantment table
     */
    @Overwrite
    override fun removed(pPlayer: Player) {
        super.removed(pPlayer)
        this.localEnchShift = 0
        if (pPlayer is ServerPlayer) {
            //ModMessages.sendToPlayer(new EnchTableUpdateS2CPacket(this.localEnchShift), (ServerPlayer) pPlayer);
        }
        this.localLength = 0
        access.execute { p_39469_: Level?, p_39470_: BlockPos? ->
            this.clearContainer(
                pPlayer,
                enchantSlots
            )
        }
    }

    @Unique
    fun getChiselEnchantmentList(level: Level, tablePos: BlockPos, enchItem: ItemStack): List<EnchantmentInstance> {
        //this.random.setSeed((long) (this.enchantmentSeed.get() + pEnchantSlot));
        val list: MutableList<EnchantmentInstance> = ArrayList()
        for (blockpos in EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
            if (level.getBlockState(tablePos.offset(blockpos)).block == Blocks.CHISELED_BOOKSHELF) {
                val bookShelf = level.getBlockEntity(tablePos.offset(blockpos)) as ChiseledBookShelfBlockEntity?
                var i = 5
                while (i > -1) {
                    if (!bookShelf!!.getItem(i).isEmpty) {
                        for (enchantment in EnchantmentHelper.getEnchantments(bookShelf.getItem(i)).keys) {
                            if (checkValidEquipmentSlot(enchItem, enchantment)) {
                                var add = true
                                for (enchantment1 in list) {
                                    if (enchantment1.enchantment == enchantment) {
                                        add = false
                                        break
                                    }
                                }
                                if (add) {
                                    list.add(EnchantmentInstance(enchantment, 1))
                                }
                            }
                        }
                    }
                    --i
                }
            }
        }
        if (list.size != this.localLength) {
            this.localEnchShift = 0
        }
        this.localLength = list.size
        return list
    }

    private fun checkValidEquipmentSlot(pStack: ItemStack, enchantment: Enchantment): Boolean {
        if (pStack.isEnchantable && checkValid(pStack)) {
            if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.MAINHAND) {
                if (pStack.item == Items.BOW) {
                    var add = false
                    for (checkEnchant in BOW_ENCHANTS) {
                        if (enchantment === checkEnchant) {
                            add = true
                            break
                        }
                    }
                    return add
                } else if (pStack.item == Items.CROSSBOW) {
                    var add = false
                    for (checkEnchant in CROSSBOW_ENCHANTS) {
                        if (enchantment === checkEnchant) {
                            add = true
                            break
                        }
                    }
                    return add
                } else if (pStack.item == Items.TRIDENT) {
                    var add = false
                    for (checkEnchant in TRIDENT_ENCHANTS) {
                        if (enchantment === checkEnchant) {
                            add = true
                            break
                        }
                    }
                    return add
                } else if (pStack.item.toString().contains("sword") ||
                    pStack.item.toString().contains("scythe") ||
                    pStack.item.toString().contains("glaive") ||
                    pStack.item.toString().contains("halberd") ||
                    pStack.item.toString().contains("hammer") ||
                    pStack.item.toString().contains("rapier") ||
                    pStack.item.toString().contains("spear") ||
                    pStack.item.toString().contains("katana") ||
                    pStack.item.toString().contains("mace")
                ) {
                    var add = false
                    for (checkEnchant in WEAPON_ENCHANTS) {
                        if (enchantment === checkEnchant) {
                            add = true
                            break
                        }
                    }
                    return add
                } else if (pStack.item.toString().contains("axe") ||
                    pStack.item.toString().contains("hoe") ||
                    pStack.item.toString().contains("shovel")
                ) {
                    var add = false
                    for (checkEnchant in TOOL_ENCHANTS) {
                        if (enchantment === checkEnchant) {
                            add = true
                            break
                        }
                    }
                    return add
                }
            } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.HEAD) {
                var add = false
                for (checkEnchant in HELM_ENCHANTS) {
                    if (enchantment === checkEnchant) {
                        add = true
                        break
                    }
                }
                return add
            } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.CHEST) {
                var add = false
                for (checkEnchant in CHEST_ENCHANTS) {
                    if (enchantment === checkEnchant) {
                        add = true
                        break
                    }
                }
                return add
            } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.LEGS) {
                var add = false
                for (checkEnchant in LEG_ENCHANTS) {
                    if (enchantment === checkEnchant) {
                        add = true
                        break
                    }
                }
                return add
            } else if (LivingEntity.getEquipmentSlotForItem(pStack) == EquipmentSlot.FEET) {
                var add = false
                for (checkEnchant in BOOT_ENCHANTS) {
                    if (enchantment === checkEnchant) {
                        add = true
                        break
                    }
                }
                return add
            }
        }
        return false
    }

    @Unique
    private fun updateEnchantmentLists() {
        /*
        if (HELM_ENCHANTS.isEmpty()) {
            HELM_ENCHANTS.add(ModEnchantments.BLACK_HOLE.get());
            HELM_ENCHANTS.add(ModEnchantments.BLIZZARD.get());
            HELM_ENCHANTS.add(ModEnchantments.FLAMETHROWER.get());
            HELM_ENCHANTS.add(ModEnchantments.SMITE.get());
            HELM_ENCHANTS.add(ModEnchantments.WARDEN_SCREAM.get());
        }
        if (CHEST_ENCHANTS.isEmpty()) {
            CHEST_ENCHANTS.add(ModEnchantments.CONCUSSION.get());
            CHEST_ENCHANTS.add(ModEnchantments.FLARE.get());
            CHEST_ENCHANTS.add(ModEnchantments.PARRY.get());
            CHEST_ENCHANTS.add(ModEnchantments.PLANAR_SHIFT.get());
            CHEST_ENCHANTS.add(ModEnchantments.WARDENSPINE.get());
        }
        if (LEG_ENCHANTS.isEmpty()) {
            LEG_ENCHANTS.add(ModEnchantments.TEMPEST.get());
            LEG_ENCHANTS.add(ModEnchantments.CRUSH.get());
            LEG_ENCHANTS.add(ModEnchantments.INCAPACITATE.get());
            LEG_ENCHANTS.add(ModEnchantments.JUDGEMENT.get());
            LEG_ENCHANTS.add(ModEnchantments.TRAIN_DASH.get());
        }
        if (BOOT_ENCHANTS.isEmpty()) {
            BOOT_ENCHANTS.add(ModEnchantments.COMET_STRIKE.get());
            BOOT_ENCHANTS.add(ModEnchantments.DOUBLE_JUMP.get());
            BOOT_ENCHANTS.add(ModEnchantments.MOMENTUM.get());
            BOOT_ENCHANTS.add(ModEnchantments.STONE_CALLER.get());
        }
        if (WEAPON_ENCHANTS.isEmpty()) {
            WEAPON_ENCHANTS.add(ModEnchantments.BURST.get());
            WEAPON_ENCHANTS.add(ModEnchantments.BREAKTHROUGH.get());
            WEAPON_ENCHANTS.add(ModEnchantments.FINESSE.get());
            WEAPON_ENCHANTS.add(ModEnchantments.GUST.get());
            WEAPON_ENCHANTS.add(ModEnchantments.KINETIC.get());
            WEAPON_ENCHANTS.add(ModEnchantments.PHANTOM_PAIN.get());
            WEAPON_ENCHANTS.add(ModEnchantments.SKEWERING.get());
            WEAPON_ENCHANTS.add(ModEnchantments.TEMPO_THEFT.get());
        }
        if (BOW_ENCHANTS.isEmpty()) {
            BOW_ENCHANTS.add(ModEnchantments.ACCELERATE.get());
            BOW_ENCHANTS.add(ModEnchantments.PHASING.get());
            BOW_ENCHANTS.add(ModEnchantments.WHISTLER.get());
        }
        if (CROSSBOW_ENCHANTS.isEmpty()) {
            CROSSBOW_ENCHANTS.add(ModEnchantments.MITOSIS.get());
            CROSSBOW_ENCHANTS.add(ModEnchantments.REND.get());
            CROSSBOW_ENCHANTS.add(ModEnchantments.SCATTER.get());
        }
        if (TRIDENT_ENCHANTS.isEmpty()) {
            TRIDENT_ENCHANTS.add(ModEnchantments.EXTRACT.get());
            TRIDENT_ENCHANTS.add(ModEnchantments.RECOIL.get());
            TRIDENT_ENCHANTS.add(Enchantments.RIPTIDE);
        }
        if (TOOL_ENCHANTS.isEmpty()) {
            TOOL_ENCHANTS.add(Enchantments.SILK_TOUCH);
            TOOL_ENCHANTS.add(ModEnchantments.BRUTE_TOUCH.get());
        }*/
    }

    companion object {
        @Unique
        private val HELM_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val CHEST_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val LEG_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val BOOT_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val WEAPON_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val BOW_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val CROSSBOW_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val TRIDENT_ENCHANTS: List<Enchantment> = ArrayList()

        @Unique
        private val TOOL_ENCHANTS: List<Enchantment> = ArrayList()
    }
}
