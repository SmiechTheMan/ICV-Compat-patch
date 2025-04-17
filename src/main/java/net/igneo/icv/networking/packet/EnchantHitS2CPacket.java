package net.igneo.icv.networking.packet;

import net.igneo.icv.init.ICVUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class EnchantHitS2CPacket {
    public EnchantHitS2CPacket() {
    
    }
    
    public EnchantHitS2CPacket(FriendlyByteBuf buf) {
    
    }
    
    public void toBytes(FriendlyByteBuf buf) {
    
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(ICVUtils::clientCooldownDamageBonuses);
        return true;
    }
}
