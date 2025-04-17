package net.igneo.icv.entity;

import net.igneo.icv.entity.boots.soulEmber.SoulEmberRenderer;
import net.igneo.icv.entity.boots.stonePillar.StonePillarRenderer;
import net.igneo.icv.entity.boots.surfWave.SurfWaveRenderer;
import net.igneo.icv.entity.chestplate.abyssStone.AbyssStoneRenderer;
import net.igneo.icv.entity.chestplate.meteorSummoner.MeteorSummonerRenderer;
import net.igneo.icv.entity.chestplate.soulOrb.SoulOrbRenderer;
import net.igneo.icv.entity.chestplate.soulSpider.SoulSpiderRenderer;
import net.igneo.icv.entity.helmet.blackHole.BlackHoleRenderer;
import net.igneo.icv.entity.helmet.divineLightningRod.DivineLightningRodRenderer;
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpike.IceSpikeRenderer;
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner.IceSpikeSpawnerRenderer;
import net.igneo.icv.entity.leggings.voidSpike.VoidSpikeRenderer;
import net.igneo.icv.entity.leggings.wave.WaveRenderer;
import net.igneo.icv.entity.weapon.FireRing.FireRingRenderer;
import net.igneo.icv.entity.weapon.boostCharge.BoostChargeRenderer;
import net.igneo.icv.entity.weapon.comet.CometRenderer;
import net.igneo.icv.entity.weapon.ember.EmberRenderer;
import net.igneo.icv.entity.weapon.snakeBite.SnakeBiteRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityRenderers {
    private static <T extends ICVEntity> void registerRenderer(RegistryObject<EntityType<T>> entity, EntityRendererProvider<T> renderer) {
        EntityRenderers.register(entity.get(), renderer);
    }
    
    public static void register() {
        registerRenderer(ModEntities.COMET, CometRenderer::new);
        registerRenderer(ModEntities.EMBER, EmberRenderer::new);
        registerRenderer(ModEntities.FIRE_RING, FireRingRenderer::new);
        registerRenderer(ModEntities.BLACK_HOLE, BlackHoleRenderer::new);
        registerRenderer(ModEntities.SNAKE_BITE, SnakeBiteRenderer::new);
        registerRenderer(ModEntities.SOUL_ORB, SoulOrbRenderer::new);
        registerRenderer(ModEntities.SOUL_SPIDER, SoulSpiderRenderer::new);
        registerRenderer(ModEntities.SURF_WAVE, SurfWaveRenderer::new);
        registerRenderer(ModEntities.STONE_PILLAR, StonePillarRenderer::new);
        registerRenderer(ModEntities.SOUL_EMBER, SoulEmberRenderer::new);
        registerRenderer(ModEntities.ABYSS_STONE, AbyssStoneRenderer::new);
        registerRenderer(ModEntities.WAVE, WaveRenderer::new);
        registerRenderer(ModEntities.VOID_SPIKE, VoidSpikeRenderer::new);
        registerRenderer(ModEntities.METEOR_SUMMONER, MeteorSummonerRenderer::new);
        registerRenderer(ModEntities.ICE_SPIKE, IceSpikeRenderer::new);
        registerRenderer(ModEntities.ICE_SPIKE_SPAWNER, IceSpikeSpawnerRenderer::new);
        registerRenderer(ModEntities.DIVINE_LIGHTNING_ROD, DivineLightningRodRenderer::new);
        registerRenderer(ModEntities.BOOST_CHARGE, BoostChargeRenderer::new);
    }
}
