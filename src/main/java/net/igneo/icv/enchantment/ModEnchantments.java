package net.igneo.icv.enchantment;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.armor.boots.*;
import net.igneo.icv.enchantment.armor.chestplate.*;
import net.igneo.icv.enchantment.armor.helmet.BlackHoleEnchantment;
import net.igneo.icv.enchantment.armor.helmet.RiftRipperEnchantment;
import net.igneo.icv.enchantment.armor.helmet.VolcanoEnchantment;
import net.igneo.icv.enchantment.armor.leggings.GaleEnchantment;
import net.igneo.icv.enchantment.armor.leggings.JudgementEnchantment;
import net.igneo.icv.enchantment.armor.leggings.TempestEnchantment;
import net.igneo.icv.enchantment.armor.leggings.TsunamiEnchantment;
import net.igneo.icv.enchantment.weapon.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ICV.MOD_ID);

    public static RegistryObject<Enchantment> BRUTE_TOUCH =
            ENCHANTMENTS.register("brute_touch",
                    () -> new BurstEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));



    public static RegistryObject<Enchantment> CASCADE =
            ENCHANTMENTS.register("cascade",
                    () -> new CascadeEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> BURST =
            ENCHANTMENTS.register("burst",
                    () -> new BurstEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> TUNGESTEN_CORE =
            ENCHANTMENTS.register("tungesten_core",
                    () -> new TungstenCoreEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> FINESSE =
            ENCHANTMENTS.register("finesse",
                    () -> new FinesseEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> GUST =
            ENCHANTMENTS.register("gust",
                    () -> new GustEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> VIPER =
            ENCHANTMENTS.register("viper",
                    () -> new ViperEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> COMET_STRIKE =
            ENCHANTMENTS.register("comet_strike",
                    () -> new CometStrikeEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> KINETIC =
            ENCHANTMENTS.register("kinetic",
                    () -> new KineticEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));



    public static RegistryObject<Enchantment> BLACK_HOLE =
            ENCHANTMENTS.register("black_hole",
                    () -> new BlackHoleEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static RegistryObject<Enchantment> RIFT_RIPPER =
            ENCHANTMENTS.register("rift_ripper",
                    () -> new RiftRipperEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static RegistryObject<Enchantment> VOLCANO =
            ENCHANTMENTS.register("volcano",
                    () -> new VolcanoEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.FEET));



    public static RegistryObject<Enchantment> PLANAR_SHIFT =
            ENCHANTMENTS.register("planar_shift",
                    () -> new PlanarShiftEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> HAUNT =
            ENCHANTMENTS.register("haunt",
                    () -> new HauntEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> MILKY_CHRYSALIS =
            ENCHANTMENTS.register("milky_chrysalis",
                    () -> new MilkyChrysalisEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> IMMOLATE =
            ENCHANTMENTS.register("immolate",
                    () -> new ImmolateEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> ABYSS_OMEN =
      ENCHANTMENTS.register("abyss_omen",
        () -> new AbyssOmenEnchantment(Enchantment.Rarity.UNCOMMON,
          EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));



    public static RegistryObject<Enchantment> TEMPEST =
            ENCHANTMENTS.register("tempest",
                    () -> new TempestEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static RegistryObject<Enchantment> JUDGEMENT =
            ENCHANTMENTS.register("judgement",
                    () -> new JudgementEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static RegistryObject<Enchantment> GALE =
            ENCHANTMENTS.register("gale",
                    () -> new GaleEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static RegistryObject<Enchantment> TSUNAMI =
            ENCHANTMENTS.register("tsunami",
                    () -> new TsunamiEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));



    public static RegistryObject<Enchantment> STASIS =
            ENCHANTMENTS.register("stasis",
                    () -> new StasisEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> SURF =
            ENCHANTMENTS.register("surf",
                    () -> new SurfEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> BLINK =
            ENCHANTMENTS.register("blink",
                    () -> new BlinkEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> STONE_CALLER =
            ENCHANTMENTS.register("stone_caller",
                    () -> new StoneCallerEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> CURB_STOMP =
            ENCHANTMENTS.register("curb_stomp",
                    () -> new CurbStompEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> SOUL_EMBER =
            ENCHANTMENTS.register("soul_ember",
                    () -> new SoulEmberEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    
    
    
    
    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }

}
