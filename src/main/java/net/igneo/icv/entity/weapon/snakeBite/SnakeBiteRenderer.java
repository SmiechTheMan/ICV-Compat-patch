package net.igneo.icv.entity.weapon.snakeBite;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SnakeBiteRenderer extends GeoEntityRenderer<SnakeBiteEntity> {

    public SnakeBiteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnakeBiteModel());
    }
}
