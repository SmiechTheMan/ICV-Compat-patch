package net.igneo.icv.config

import net.minecraftforge.common.ForgeConfigSpec

object ICVClientConfigs {

    private val builder = ForgeConfigSpec.Builder()

    private val helmetX: ForgeConfigSpec.ConfigValue<Int>
    private val helmetY: ForgeConfigSpec.ConfigValue<Int>

    private val chestplateX: ForgeConfigSpec.ConfigValue<Int>
    private val chestplateY: ForgeConfigSpec.ConfigValue<Int>

    private val leggingsX: ForgeConfigSpec.ConfigValue<Int>
    private val leggingsY: ForgeConfigSpec.ConfigValue<Int>

    @JvmField
    val spec: ForgeConfigSpec

    init {
        builder.push("ICV CONFIGS")

        helmetX = builder
            .comment("Offset on the X axis for helmet cooldown indicators")
            .define("helmetXOffset", 0)

        helmetY = builder
            .comment("Offset on the Y axis for helmet cooldown indicators")
            .define("helmetYOffset", 0)

        chestplateX = builder
            .comment("Offset on the X axis for chestplate cooldown indicators")
            .define("chestplateXOffset", 0)

        chestplateY = builder
            .comment("Offset on the Y axis for chestplate cooldown indicators")
            .define("chestplateYOffset", 0)

        leggingsX = builder
            .comment("Offset on the X axis for leggings cooldown indicators")
            .define("leggingsXOffset", 0)

        leggingsY = builder
            .comment("Offset on the Y axis for leggings cooldown indicators")
            .define("leggingsYOffset", 0)

        builder.pop()
        spec = builder.build()
    }
}

object ICVCommonConfigs {

    private val builder = ForgeConfigSpec.Builder()

    @JvmField
    val spec: ForgeConfigSpec

    @JvmField
    val trimEffects: ForgeConfigSpec.ConfigValue<Boolean>

    init {
        builder.push("ICV CONFIGS")

        trimEffects = builder
            .comment(
                "Enable trim effects?\n" +
                        "Highly recommended: Re-enables most vanilla enchantments.\n" +
                        "Disable if you want trims to be cosmetic only (use Cosmetic Armor)."
            )
            .define("enableTrimEffects", true)

        builder.pop()
        spec = builder.build()
    }
}
