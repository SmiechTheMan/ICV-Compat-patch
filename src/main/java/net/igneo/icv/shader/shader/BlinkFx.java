package net.igneo.icv.shader.shader;

import net.igneo.icv.shader.postProcessors.BlinkPostProcessor;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.postprocess.DynamicShaderFxInstance;

import java.util.function.BiConsumer;

public class BlinkFx extends DynamicShaderFxInstance {
    public Vector3f center;
    public Vector3f color;
    public float instTime = 0.0F;
    public long storedTime;
    public double deltaTime;
    public BlinkPostProcessor parent;
    
    public BlinkFx(Vector3f center, Vector3f color) {
        this.center = center;
        this.color = color;
        this.instTime = 0;
    }
    
    @Override
    public void update(double deltaTime) {
        
        if (this.instTime * 2 >= 1) {
            this.remove();
        }
        this.deltaTime = (System.nanoTime() - storedTime) / 1_000_000_000.0f;
        storedTime = System.nanoTime();
        this.instTime = (float) ((double) this.instTime + this.deltaTime);
    }
    
    @Override
    public void writeDataToBuffer(BiConsumer<Integer, Float> writer) {
        writer.accept(0, center.x());
        writer.accept(1, center.y());
        writer.accept(2, center.z());
        writer.accept(3, color.x());
        writer.accept(4, color.y());
        writer.accept(5, color.z());
        writer.accept(6, instTime);
    }
}
