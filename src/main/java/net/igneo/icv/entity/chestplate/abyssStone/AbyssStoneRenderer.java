package net.igneo.icv.entity.chestplate.abyssStone;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AbyssStoneRenderer extends GeoEntityRenderer<AbyssStoneEntity> {
    public AbyssStoneRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbyssStoneModel());
    }
}
