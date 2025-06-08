package net.igneo.icv.entity

import net.igneo.icv.entity.boots.soulEmber.SoulEmberRenderer
import net.igneo.icv.entity.boots.stonePillar.StonePillarRenderer
import net.igneo.icv.entity.boots.surfWave.SurfWaveRenderer
import net.igneo.icv.entity.chestplate.abyssStone.AbyssStoneRenderer
import net.igneo.icv.entity.chestplate.meteorSummoner.MeteorSummonerRenderer
import net.igneo.icv.entity.chestplate.soulOrb.SoulOrbRenderer
import net.igneo.icv.entity.chestplate.soulSpider.SoulSpiderRenderer
import net.igneo.icv.entity.helmet.blackHole.BlackHoleRenderer
import net.igneo.icv.entity.helmet.divineLightningRod.DivineLightningRodRenderer
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpike.IceSpikeRenderer
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner.IceSpikeSpawnerRenderer
import net.igneo.icv.entity.leggings.voidSpike.VoidSpikeRenderer
import net.igneo.icv.entity.leggings.wave.WaveRenderer
import net.igneo.icv.entity.weapon.boostCharge.BoostChargeRenderer
import net.igneo.icv.entity.weapon.comet.CometRenderer
import net.igneo.icv.entity.weapon.ember.EmberRenderer
import net.igneo.icv.entity.weapon.fireRing.FireRingRenderer
import net.igneo.icv.entity.weapon.snakeBite.SnakeBiteRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.entity.EntityType
import net.minecraftforge.registries.RegistryObject

object ModEntityRenderers {
    private fun <T : ICVEntity?> registerRenderer(
        entity: RegistryObject<EntityType<T>>,
        renderer: EntityRendererProvider<T>
    ) {
        EntityRenderers.register(entity.get(), renderer)
    }

    fun register() {
        registerRenderer(ModEntities.COMET) { renderManager: EntityRendererProvider.Context? -> CometRenderer(renderManager!!) }
        registerRenderer(ModEntities.EMBER) { renderManager: EntityRendererProvider.Context? -> EmberRenderer(renderManager!!) }
        registerRenderer(ModEntities.FIRE_RING) { renderManager: EntityRendererProvider.Context? -> FireRingRenderer(renderManager!!) }
        registerRenderer(ModEntities.BLACK_HOLE) { renderManager: EntityRendererProvider.Context? -> BlackHoleRenderer(renderManager!!) }
        registerRenderer(ModEntities.SNAKE_BITE) { renderManager: EntityRendererProvider.Context? -> SnakeBiteRenderer(renderManager!!) }
        registerRenderer(ModEntities.SOUL_ORB) { renderManager: EntityRendererProvider.Context? -> SoulOrbRenderer(renderManager!!) }
        registerRenderer(ModEntities.SOUL_SPIDER) { renderManager: EntityRendererProvider.Context? -> SoulSpiderRenderer(renderManager!!) }
        registerRenderer(ModEntities.SURF_WAVE) { renderManager: EntityRendererProvider.Context? -> SurfWaveRenderer(renderManager!!) }
        registerRenderer(ModEntities.STONE_PILLAR) { renderManager: EntityRendererProvider.Context? -> StonePillarRenderer(renderManager!!) }
        registerRenderer(ModEntities.SOUL_EMBER) { renderManager: EntityRendererProvider.Context? -> SoulEmberRenderer(renderManager!!) }
        registerRenderer(ModEntities.ABYSS_STONE) { renderManager: EntityRendererProvider.Context? -> AbyssStoneRenderer(renderManager!!) }
        registerRenderer(ModEntities.WAVE) { renderManager: EntityRendererProvider.Context? -> WaveRenderer(renderManager!!) }
        registerRenderer(ModEntities.VOID_SPIKE) { renderManager: EntityRendererProvider.Context? -> VoidSpikeRenderer(renderManager!!) }
        registerRenderer(ModEntities.METEOR_SUMMONER) { renderManager: EntityRendererProvider.Context? -> MeteorSummonerRenderer(renderManager!!) }
        registerRenderer(ModEntities.ICE_SPIKE) { renderManager: EntityRendererProvider.Context? -> IceSpikeRenderer(renderManager!!) }
        registerRenderer(ModEntities.ICE_SPIKE_SPAWNER) { renderManager: EntityRendererProvider.Context? -> IceSpikeSpawnerRenderer(renderManager!!) }
        registerRenderer(ModEntities.DIVINE_LIGHTNING_ROD) { renderManager: EntityRendererProvider.Context? -> DivineLightningRodRenderer(renderManager!!) }
        registerRenderer(ModEntities.BOOST_CHARGE) { renderManager: EntityRendererProvider.Context? -> BoostChargeRenderer(renderManager!!) }
    }
}
