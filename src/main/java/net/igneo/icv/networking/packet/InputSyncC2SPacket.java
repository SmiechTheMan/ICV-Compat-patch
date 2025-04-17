package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InputSyncC2SPacket {
    private final int ID;
    
    public InputSyncC2SPacket(int ID) {
        this.ID = ID;
    }
    
    public InputSyncC2SPacket(FriendlyByteBuf buf) {
        ID = buf.readInt();
    }
    
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ID);
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                enchVar.input = Input.getInput(ID);
            });
        });
        return true;
    }
}