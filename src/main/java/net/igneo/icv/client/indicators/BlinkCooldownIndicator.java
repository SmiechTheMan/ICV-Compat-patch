package net.igneo.icv.client.indicators;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class BlinkCooldownIndicator extends EnchantIndicator {
    private static final ResourceLocation STASIS = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/blink.png");
    
    public BlinkCooldownIndicator(ArmorEnchantManager manager) {
        super(24, 16, 20, 3, STASIS, manager);
    }
}
