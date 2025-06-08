package net.igneo.icv.entity.weapon.ember

import net.igneo.icv.entity.ICVEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3


class EmberEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    //protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    var hurt: Double = 0.0
    var hurtTicks: Int = 0
    var hurtPos: Vec3? = null
    var peaked: Boolean = false

    override fun onHitBlock(pResult: BlockHitResult) {
        val sLevel = level() as ServerLevel
        if (level() is ServerLevel) {
            sLevel.setBlock(pResult.blockPos, Blocks.FIRE.defaultBlockState(), 4)
        }
        this.discard()
        super.onHitBlock(pResult)
    }

    override fun tick() {
        if (onGround()) {
            val sLevel = level() as ServerLevel
            if (level() is ServerLevel) {
                sLevel.setBlock(blockPosition(), Blocks.FIRE.defaultBlockState(), 2)
            }
            this.discard()
        }
        super.tick()
    }
}
