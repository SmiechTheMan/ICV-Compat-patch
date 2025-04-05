package net.igneo.icv.entity.weapon.ember;


import net.igneo.icv.ICV;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class EmberEntity extends ICVEntity {
    //protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    public double hurt = 0;
    public int hurtTicks;
    public Vec3 hurtPos;
    public boolean peaked = false;
    public EmberEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if(level() instanceof ServerLevel sLevel){
            sLevel.setBlock(pResult.getBlockPos(), Blocks.FIRE.defaultBlockState(),4);
        }
        this.discard();
        super.onHitBlock(pResult);
    }

    @Override
    public void tick() {
        if(onGround()){
            if(level() instanceof ServerLevel sLevel){
                sLevel.setBlock(blockPosition(), Blocks.FIRE.defaultBlockState(),2);
            }
            this.discard();
        }
        super.tick();
    }
}
