package net.igneo.icv.entity;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.abyssStone.AbyssStoneEntity;
import net.igneo.icv.entity.blackHole.BlackHoleEntity;
import net.igneo.icv.entity.comet.CometEntity;
import net.igneo.icv.entity.snakeBite.SnakeBiteEntity;
import net.igneo.icv.entity.soulEmber.SoulEmberEntity;
import net.igneo.icv.entity.soulOrb.SoulOrbEntity;
import net.igneo.icv.entity.soulSpider.SoulSpiderEntity;
import net.igneo.icv.entity.stonePillar.StonePillarEntity;
import net.igneo.icv.entity.surfWave.SurfWaveEntity;
import net.igneo.icv.entity.voidSpike.VoidSpikeEntity;
import net.igneo.icv.entity.wave.WaveEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ICV.MOD_ID);

    public static final RegistryObject<EntityType<CometEntity>> COMET =
            ENTITY_TYPES.register("comet",() -> EntityType.Builder.<CometEntity>of(CometEntity::new, MobCategory.MISC)
                    .sized(0.5f,0.5f).build("comet"));
    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE =
            ENTITY_TYPES.register("black_hole",() -> EntityType.Builder.<BlackHoleEntity>of(BlackHoleEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("black_hole"));
    public static final RegistryObject<EntityType<SnakeBiteEntity>> SNAKE_BITE =
            ENTITY_TYPES.register("snake_bite",() -> EntityType.Builder.<SnakeBiteEntity>of(SnakeBiteEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("snake_bite"));
    public static final RegistryObject<EntityType<SoulOrbEntity>> SOUL_ORB =
            ENTITY_TYPES.register("soul_orb",() -> EntityType.Builder.<SoulOrbEntity>of(SoulOrbEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("soul_orb"));
    public static final RegistryObject<EntityType<SoulSpiderEntity>> SOUL_SPIDER =
            ENTITY_TYPES.register("soul_spider",() -> EntityType.Builder.<SoulSpiderEntity>of(SoulSpiderEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("soul_spider"));
    public static final RegistryObject<EntityType<SurfWaveEntity>> SURF_WAVE =
            ENTITY_TYPES.register("surf_wave",() -> EntityType.Builder.<SurfWaveEntity>of(SurfWaveEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("surf_wave"));
    public static final RegistryObject<EntityType<StonePillarEntity>> STONE_PILLAR =
            ENTITY_TYPES.register("stone_pillar",() -> EntityType.Builder.<StonePillarEntity>of(StonePillarEntity::new, MobCategory.MISC)
                    .sized(2.5f,4f).build("stone_pillar"));
    public static final RegistryObject<EntityType<SoulEmberEntity>> SOUL_EMBER =
            ENTITY_TYPES.register("soul_ember",() -> EntityType.Builder.<SoulEmberEntity>of(SoulEmberEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("soul_ember"));
    public static final RegistryObject<EntityType<AbyssStoneEntity>> ABYSS_STONE =
      ENTITY_TYPES.register("abyss_stone",() -> EntityType.Builder.<AbyssStoneEntity>of(AbyssStoneEntity::new, MobCategory.MISC)
        .sized(2.5f,4f).build("abyss_stone"));
    public static final RegistryObject<EntityType<WaveEntity>> WAVE =
            ENTITY_TYPES.register("wave",() -> EntityType.Builder.<WaveEntity>of(WaveEntity::new, MobCategory.MISC)
                    .sized(2.5f,4f).build("wave"));
    public static final RegistryObject<EntityType<VoidSpikeEntity>> VOID_SPIKE =
            ENTITY_TYPES.register("void_spike",() -> EntityType.Builder.<VoidSpikeEntity>of(VoidSpikeEntity::new, MobCategory.MISC)
                    .sized(2.5f,4f).build("void_spike"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}