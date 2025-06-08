package net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class IceSpikeSpawnerRenderer(renderManager: EntityRendererProvider.Context) :
    GeoEntityRenderer<IceSpikeSpawnerEntity?>(renderManager, iceSpikeSpawnerModel())
