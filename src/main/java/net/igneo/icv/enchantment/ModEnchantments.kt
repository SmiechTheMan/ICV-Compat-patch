package net.igneo.icv.enchantment

import net.igneo.icv.ICV
import net.igneo.icv.enchantment.tool.BruteTouchEnchantment
import net.igneo.icv.enchantment.trident.*
import net.igneo.icv.enchantment.weapon.*
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEnchantments {

    val ENCHANTMENTS: DeferredRegister<Enchantment> =
        DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ICV.MOD_ID)

    private fun <T : ICVEnchantment> registerEnchantment(
        name: String,
        factory: () -> T
    ): RegistryObject<Enchantment> = ENCHANTMENTS.register(name, factory)

    object Tools {
        val BRUTE_TOUCH: RegistryObject<Enchantment> = registerEnchantment("brute_touch") { BruteTouchEnchantment() }
    }

    object Weapons {
        val CASCADE = registerEnchantment("cascade") { CascadeEnchantment() }
        val BURST = registerEnchantment("burst") { BurstEnchantment() }
        val TUNGSTEN_CORE = registerEnchantment("tungsten_core") { TungstenCoreEnchantment() }
        val FINESSE = registerEnchantment("finesse") { FinesseEnchantment() }
        val GUST = registerEnchantment("gust") { GustEnchantment() }
        val VIPER = registerEnchantment("viper") { ViperEnchantment() }
        val COMET_STRIKE = registerEnchantment("comet_strike") { CometStrikeEnchantment() }
        val KINETIC = registerEnchantment("kinetic") { KineticEnchantment() }
        val VOLATILE = registerEnchantment("volatile") { VolatileEnchantment() }
        val MOLTEN = registerEnchantment("molten") { MoltenEnchantment() }
        val BREAKTHROUGH = registerEnchantment("breakthrough") { BreakthroughEnchantment() }
        val MEATHOOK = registerEnchantment("meathooks") { MeathookEnchantment() }
    }

    object Tridents {
        val GEYSER = registerEnchantment("geyser") { GeyserEnchantment() }
        val WHIRLPOOL = registerEnchantment("whirlpool") { WhirlpoolEnchantment() }
        val UPWELL = registerEnchantment("upwell") { UpwellEnchantment() }
        val CAVITATION = registerEnchantment("cavitation") { CavitationEnchantment() }
        val UNDERTOW = registerEnchantment("undertow") { UndertowEnchantment() }
    }


    object Armor {
        object Helmet {
            val BLACK_HOLE = registerEnchantment("black_hole") { BlackHoleEnchantment() }
            val RIFT_RIPPER = registerEnchantment("rift_ripper") { RiftRipperEnchantment() }
            val GRAVITY_WELL = registerEnchantment("gravity_well") { GravityWellEnchantment() }
            val GLACIAL_IMPASSE = registerEnchantment("glacial_impasse") { GlacialImpasseEnchantment() }
            val DIVINE_SMITE = registerEnchantment("divine_smite") { DivineSmiteEnchantment() }
            val VOLCANO = registerEnchantment("volcano") { VolcanoEnchantment() }
        }

        object Chestplate {
            val PLANAR_SHIFT = registerEnchantment("planar_shift") { PlanarShiftEnchantment() }
            val HAUNT = registerEnchantment("haunt") { HauntEnchantment() }
            val MILKY_CHRYSALIS = registerEnchantment("milky_chrysalis") { MilkyChrysalisEnchantment() }
            val IMMOLATE = registerEnchantment("immolate") { ImmolateEnchantment() }
            val EXTINCTION = registerEnchantment("extinction") { ExtinctionEnchantment() }
            val ABYSS_OMEN = registerEnchantment("abyss_omen") { AbyssOmenEnchantment() }
        }

        object Leggings {
            val TEMPEST = registerEnchantment("tempest") { TempestEnchantment() }
            val JUDGEMENT = registerEnchantment("judgement") { JudgementEnchantment() }
            val GALE = registerEnchantment("gale") { GaleEnchantment() }
            val TSUNAMI = registerEnchantment("tsunami") { TsunamiEnchantment() }
            val VOID_WAKE = registerEnchantment("void_wake") { VoidWakeEnchantment() }
            val HURRICANE = registerEnchantment("hurricane") { HurricaneEnchantment() }
        }

        object Boots {
            val STASIS = registerEnchantment("stasis") { StasisEnchantment() }
            val SURF = registerEnchantment("surf") { SurfEnchantment() }
            val BLINK = registerEnchantment("blink") { BlinkEnchantment() }
            val STONE_CALLER = registerEnchantment("stone_caller") { StoneCallerEnchantment() }
            val CURB_STOMP = registerEnchantment("curb_stomp") { CurbStompEnchantment() }
            val SOUL_EMBER = registerEnchantment("soul_ember") { SoulEmberEnchantment() }
        }
    }

    fun register(eventBus: IEventBus) {
        ENCHANTMENTS.register(eventBus)
    }
}