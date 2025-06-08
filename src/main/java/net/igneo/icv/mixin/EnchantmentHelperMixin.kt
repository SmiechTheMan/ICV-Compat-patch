package net.igneo.icv.mixin

import net.igneo.icv.config.ICVCommonConfigs
import net.igneo.icv.enchantment.ModEnchantments.Tools.BRUTE_TOUCH
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite

@Mixin(value = [EnchantmentHelper::class])
object EnchantmentHelperMixin {
    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    fun getKnockbackBonus(pPlayer: LivingEntity): Int {
        var trimCount = 0
        if (pPlayer is Player && ICVCommonConfigs.TRIM_EFFECTS.get()) {
            for (j in 0..3) {
                if (!pPlayer.inventory.getArmor(j).toString().contains("air") && pPlayer.inventory.getArmor(j)
                        .serializeNBT().toString().contains("tag")
                ) {
                    if (pPlayer.inventory.getArmor(j).tag!!.allKeys.contains("Trim")) {
                        val tag = pPlayer.inventory.getArmor(j).tag!!["Trim"]
                        if (tag.toString().contains("sentry")) {
                            ++trimCount
                        }
                    }
                }
            }
        }
        return trimCount
    }

    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    fun getDamageProtection(pStacks: Iterable<ItemStack>, pSource: DamageSource): Int {
        var protInt = 0
        if (ICVCommonConfigs.TRIM_EFFECTS.get()) {
            if (pSource.`is`(DamageTypes.IN_FIRE) || pSource.`is`(DamageTypes.ON_FIRE) || pSource.`is`(DamageTypes.LAVA)) {
                for (pStack in pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.tag!!.allKeys.contains("Trim")) {
                            val tag = pStack.tag!!["Trim"]
                            if (tag.toString().contains("rib")) {
                                protInt += 3
                            }
                        }
                    }
                }
            }
            if (pSource.`is`(DamageTypes.MAGIC) || pSource.`is`(DamageTypes.INDIRECT_MAGIC) || pSource.`is`(DamageTypes.DRAGON_BREATH)
                || pSource.`is`(DamageTypes.SONIC_BOOM) || pSource.`is`(DamageTypes.LIGHTNING_BOLT) || pSource.`is`(
                    DamageTypes.WITHER
                )
            ) {
                for (pStack in pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.tag!!.allKeys.contains("Trim")) {
                            val tag = pStack.tag!!["Trim"]
                            if (tag.toString().contains("eye")) {
                                protInt += 3
                            }
                        }
                    }
                }
            }
            if (pSource.`is`(DamageTypes.FALL)) {
                for (pStack in pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.tag!!.allKeys.contains("Trim")) {
                            val tag = pStack.tag!!["Trim"]
                            if (tag.toString().contains("spire")) {
                                protInt += 3
                            }
                        }
                    }
                }
            }
            if (pSource.`is`(DamageTypes.EXPLOSION) || pSource.`is`(DamageTypes.PLAYER_EXPLOSION) || pSource.`is`(
                    DamageTypes.BAD_RESPAWN_POINT
                )
            ) {
                for (pStack in pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.tag!!.allKeys.contains("Trim")) {
                            val tag = pStack.tag!!["Trim"]
                            if (tag.toString().contains("ward")) {
                                protInt += 3
                            }
                        }
                    }
                }
            }
            if (pSource.`is`(DamageTypes.ARROW) || pSource.`is`(DamageTypes.MOB_PROJECTILE) || pSource.`is`(DamageTypes.UNATTRIBUTED_FIREBALL)) {
                for (pStack in pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.tag!!.allKeys.contains("Trim")) {
                            val tag = pStack.tag!!["Trim"]
                            if (tag.toString().contains("ward")) {
                                protInt += 3
                            }
                        }
                    }
                }
            }
            for (pStack in pStacks) {
                if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                    if (pStack.tag!!.allKeys.contains("Trim")) {
                        val tag = pStack.tag!!["Trim"]
                        if (tag.toString().contains("sentry")) {
                            protInt -= 3
                        }
                    }
                }
            }
            for (pStack in pStacks) {
                if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                    if (pStack.tag!!.allKeys.contains("Trim")) {
                        val tag = pStack.tag!!["Trim"]
                        if (tag.toString().contains("host")) {
                            protInt += 3
                        }
                    }
                }
            }
        }
        return protInt
    }

    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    fun getDepthStrider(pEntity: LivingEntity): Int {
        var trimCount = 0
        if (pEntity is Player && ICVCommonConfigs.TRIM_EFFECTS.get()) {
            for (j in 0..3) {
                if (!pEntity.inventory.getArmor(j).toString().contains("air") && pEntity.inventory.getArmor(j)
                        .serializeNBT().toString().contains("tag")
                ) {
                    if (pEntity.inventory.getArmor(j).tag!!.allKeys.contains("Trim")) {
                        val tag = pEntity.inventory.getArmor(j).tag!!["Trim"]
                        if (tag.toString().contains("tide")) {
                            ++trimCount
                        }
                    }
                }
            }
        }
        return trimCount
    }

    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    fun getRespiration(pEntity: LivingEntity): Int {
        var trimCount = 0
        if (pEntity is Player && ICVCommonConfigs.TRIM_EFFECTS.get()) {
            for (j in 0..3) {
                if (!pEntity.inventory.getArmor(j).toString().contains("air") && pEntity.inventory.getArmor(j)
                        .serializeNBT().toString().contains("tag")
                ) {
                    if (pEntity.inventory.getArmor(j).tag!!.allKeys.contains("Trim")) {
                        val tag = pEntity.inventory.getArmor(j).tag!!["Trim"]
                        if (tag.toString().contains("coast")) {
                            ++trimCount
                        }
                    }
                }
            }
        }
        return trimCount
    }

    /**
     * @author Igneo
     * @reason won't exist anymore
     */
    @Overwrite
    fun getLoyalty(pStack: ItemStack?): Int {
        return 3
    }

    /**
     * @author Igneo220
     * @reason removing efficiency adding brute touch
     */
    @Overwrite
    fun getBlockEfficiency(pEntity: LivingEntity): Int {
        return if (EnchantmentHelper.getEnchantmentLevel(BRUTE_TOUCH.get(), pEntity) == 1) {
            5
        } else {
            0
        }
    }
}
