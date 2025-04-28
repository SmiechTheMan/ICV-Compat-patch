package net.igneo.icv.client.indicators;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class SurfIndicator extends EnchantIndicator{
    private static final ResourceLocation SURF = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/surf.png");
    public SurfIndicator(ArmorEnchantManager manager) {
        super(28,15,22,3, SURF, manager);
    }
}
