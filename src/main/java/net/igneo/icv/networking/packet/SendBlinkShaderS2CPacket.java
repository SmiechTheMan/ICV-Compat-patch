package net.igneo.icv.networking.packet;

import net.igneo.icv.shader.postProcessors.BlinkPostProcessor;
import net.igneo.icv.shader.shader.BlinkFx;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SendBlinkShaderS2CPacket {

    private final double x;
    private final double y;
    private final double z;
    public SendBlinkShaderS2CPacket(Vec3 pos){
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }
    public SendBlinkShaderS2CPacket(FriendlyByteBuf buf) {
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
            Vec3 pos = new Vec3(x,y,z);;
            Vector3f center = pos.toVector3f();
            Vector3f color = new Vector3f(0.7F, 0.0F, 1);
            BlinkFx fx = new BlinkFx(center, color);
            fx.instTime = 0;
            fx.storedTime = System.nanoTime();
            fx.deltaTime = (System.nanoTime() - fx.storedTime) / 1_000_000_000.0f;
            BlinkPostProcessor.INSTANCE.addFxInstance(fx);
        });
        return true;
    }
}
