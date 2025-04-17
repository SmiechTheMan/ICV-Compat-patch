package net.igneo.icv.entity.chestplate.soulSpider;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulSpiderRenderer extends GeoEntityRenderer<SoulSpiderEntity> {
    
    public SoulSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SoulSpiderModel());
    }
}
