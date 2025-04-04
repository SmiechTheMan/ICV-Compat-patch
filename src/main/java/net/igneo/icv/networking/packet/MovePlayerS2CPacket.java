package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MovePlayerS2CPacket {

    private final double x;
    private final double y;
    private final double z;
    public MovePlayerS2CPacket(Vec3 setDirection){
        this.x = setDirection.x;
        this.y = setDirection.y;
        this.z = setDirection.z;
    }
    public MovePlayerS2CPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            double x = (this.x == 0) ? Minecraft.getInstance().player.getDeltaMovement().x : this.x;
            double z = (this.z == 0) ? Minecraft.getInstance().player.getDeltaMovement().z : this.z;
            Vec3 setVec = new Vec3(x,y,z);;
            Minecraft.getInstance().player.setDeltaMovement(setVec);
        });
        return true;
    }
}
