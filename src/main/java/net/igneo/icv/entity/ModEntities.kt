package net.igneo.icv.entity

import net.igneo.icv.ICV
import net.igneo.icv.entity.boots.soulEmber.SoulEmberEntity
import net.igneo.icv.entity.boots.stonePillar.StonePillarEntity
import net.igneo.icv.entity.boots.surfWave.SurfWaveEntity
import net.igneo.icv.entity.chestplate.abyssStone.AbyssStoneEntity
import net.igneo.icv.entity.chestplate.meteorSummoner.MeteorSummonerEntity
import net.igneo.icv.entity.chestplate.soulOrb.SoulOrbEntity
import net.igneo.icv.entity.chestplate.soulSpider.SoulSpiderEntity
import net.igneo.icv.entity.helmet.blackHole.BlackHoleEntity
import net.igneo.icv.entity.helmet.divineLightningRod.DivineLightningRodEntity
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpike.IceSpikeEntity
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner.IceSpikeSpawnerEntity
import net.igneo.icv.entity.leggings.voidSpike.VoidSpikeEntity
import net.igneo.icv.entity.leggings.wave.WaveEntity
import net.igneo.icv.entity.weapon.boostCharge.BoostChargeEntity
import net.igneo.icv.entity.weapon.comet.CometEntity
import net.igneo.icv.entity.weapon.ember.EmberEntity
import net.igneo.icv.entity.weapon.fireRing.FireRingEntity
import net.igneo.icv.entity.weapon.snakeBite.SnakeBiteEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntityType.EntityFactory
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
    DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ICV.MOD_ID)

private fun <T : Entity?> registerEntity(
    name: String,
    factory: EntityFactory<T>,
    width: Float,
    height: Float
): RegistryObject<EntityType<T>> {
    return ENTITY_TYPES.register(
        name
    ) {
        EntityType.Builder.of(factory, MobCategory.MISC)
            .sized(width, height)
            .build(name)
    }
}

val COMET: RegistryObject<EntityType<CometEntity?>> = registerEntity(
    "comet",
    { pEntityType: EntityType<CometEntity?>?, pLevel: Level? -> CometEntity(pEntityType!!, pLevel!!) },
    0.5f,
    0.5f
)
val EMBER: RegistryObject<EntityType<EmberEntity?>> = registerEntity(
    "ember",
    { pEntityType: EntityType<EmberEntity?>?, pLevel: Level? -> EmberEntity(pEntityType!!, pLevel!!) },
    0.5f,
    0.5f
)
val FIRE_RING: RegistryObject<EntityType<FireRingEntity?>> = registerEntity(
    "fire_ring",
    { pEntityType: EntityType<FireRingEntity?>?, pLevel: Level? -> FireRingEntity(pEntityType!!, pLevel!!) },
    0.5f,
    0.5f
)
val BLACK_HOLE: RegistryObject<EntityType<BlackHoleEntity?>> = registerEntity(
    "black_hole",
    { pEntityType: EntityType<BlackHoleEntity?>?, pLevel: Level? -> BlackHoleEntity(pEntityType!!, pLevel!!) },
    1f,
    1f
)
val SNAKE_BITE: RegistryObject<EntityType<SnakeBiteEntity?>> = registerEntity(
    "snake_bite",
    { pEntityType: EntityType<SnakeBiteEntity?>?, pLevel: Level? -> SnakeBiteEntity(pEntityType, pLevel) },
    1f,
    1f
)
val SOUL_ORB: RegistryObject<EntityType<SoulOrbEntity?>> = registerEntity(
    "soul_orb",
    { pEntityType: EntityType<SoulOrbEntity?>?, pLevel: Level? -> SoulOrbEntity(pEntityType, pLevel) },
    1f,
    1f
)
val SOUL_SPIDER: RegistryObject<EntityType<SoulSpiderEntity?>> = registerEntity(
    "soul_spider",
    { pEntityType: EntityType<SoulSpiderEntity?>?, pLevel: Level? -> SoulSpiderEntity(pEntityType!!, pLevel!!) },
    1f,
    1f
)
val SURF_WAVE: RegistryObject<EntityType<SurfWaveEntity?>> = registerEntity(
    "surf_wave",
    { pEntityType: EntityType<SurfWaveEntity?>?, pLevel: Level? -> SurfWaveEntity(pEntityType!!, pLevel!!) },
    1f,
    1f
)

@JvmField
val STONE_PILLAR: RegistryObject<EntityType<StonePillarEntity?>> = registerEntity(
    "stone_pillar",
    { pEntityType: EntityType<StonePillarEntity?>?, pLevel: Level? -> StonePillarEntity(pEntityType!!, pLevel!!) },
    2.5f,
    4f
)
val SOUL_EMBER: RegistryObject<EntityType<SoulEmberEntity?>> = registerEntity(
    "soul_ember",
    { pEntityType: EntityType<SoulEmberEntity?>?, pLevel: Level? -> SoulEmberEntity(pEntityType!!, pLevel!!) },
    1f,
    1f
)
val ABYSS_STONE: RegistryObject<EntityType<AbyssStoneEntity?>> = registerEntity(
    "abyss_stone",
    { pEntityType: EntityType<AbyssStoneEntity?>?, pLevel: Level? -> AbyssStoneEntity(pEntityType!!, pLevel!!) },
    2.5f,
    4f
)
val WAVE: RegistryObject<EntityType<WaveEntity?>> = registerEntity(
    "wave",
    { pEntityType: EntityType<WaveEntity?>?, pLevel: Level? -> WaveEntity(pEntityType!!, pLevel!!) },
    1f,
    1f
)
val VOID_SPIKE: RegistryObject<EntityType<VoidSpikeEntity?>> = registerEntity(
    "void_spike",
    { pEntityType: EntityType<VoidSpikeEntity?>?, pLevel: Level? -> VoidSpikeEntity(pEntityType!!, pLevel!!) },
    1f,
    1f
)
val METEOR_SUMMONER: RegistryObject<EntityType<MeteorSummonerEntity?>> = registerEntity(
    "meteor_summoner",
    { pEntityType: EntityType<MeteorSummonerEntity?>?, pLevel: Level? ->
        MeteorSummonerEntity(
            pEntityType!!,
            pLevel!!
        )
    },
    1f,
    1f
)

@JvmField
val ICE_SPIKE: RegistryObject<EntityType<IceSpikeEntity?>> = registerEntity(
    "ice_spike",
    { pEntityType: EntityType<IceSpikeEntity?>?, pLevel: Level? -> IceSpikeEntity(pEntityType!!, pLevel!!) },
    1f,
    4f
)
val ICE_SPIKE_SPAWNER: RegistryObject<EntityType<IceSpikeSpawnerEntity?>> = registerEntity(
    "ice_spike_spawner",
    { pEntityType: EntityType<IceSpikeSpawnerEntity?>?, pLevel: Level? ->
        IceSpikeSpawnerEntity(
            pEntityType!!,
            pLevel!!
        )
    },
    0f,
    0f
)
val DIVINE_LIGHTNING_ROD: RegistryObject<EntityType<DivineLightningRodEntity?>> = registerEntity(
    "divine_lightning_rod",
    { pEntityType: EntityType<DivineLightningRodEntity?>?, pLevel: Level? ->
        DivineLightningRodEntity(
            pEntityType!!,
            pLevel!!
        )
    },
    1f,
    1f
)
val BOOST_CHARGE: RegistryObject<EntityType<BoostChargeEntity?>> = registerEntity(
    "boost_charge",
    { pEntityType: EntityType<BoostChargeEntity?>?, pLevel: Level? -> BoostChargeEntity(pEntityType!!, pLevel!!) },
    0.5f,
    0.5f
)

fun registerEntities(eventBus: IEventBus?) {
    ENTITY_TYPES.register(eventBus)
}