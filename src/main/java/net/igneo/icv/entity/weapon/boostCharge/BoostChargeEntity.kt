package net.igneo.icv.entity.weapon.boostCharge

import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.init.ICVUtils.collectEntitiesBox
import net.igneo.icv.networking.ModMessages.sendToPlayer
import net.igneo.icv.networking.packet.MovePlayerS2CPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class BoostChargeEntity(pEntityType: EntityType<out Projectile?>, pLevel: Level) :
    ICVEntity(pEntityType, pLevel) {
    private var lifeTime: Int = 0

    override fun tick() {
        super.tick()
        if (lifeTime > 50) {
            this.discard()
        } else {
            ++lifeTime
        }

        if (this.onGround()) {
            for (entity in collectEntitiesBox(this.level(), position(), 3.0)) {
                entity.deltaMovement = Vec3(entity.deltaMovement.x, 0.7, entity.deltaMovement.x)
                if (entity is ServerPlayer) {
                    sendToPlayer(MovePlayerS2CPacket(Vec3(0.0, 0.7, 0.0)), entity)
                }
            }
            this.discard()
        }
    }

    override fun hurt(pSource: DamageSource, pAmount: Float): Boolean {
        this.discard()
        return true
    }

    override fun isPickable(): Boolean {
        return true
    }

    override fun onHit(pResult: HitResult) {
        if (pResult is EntityHitResult) {
            if (pResult.entity === this.owner) return
        }
        for (entity in collectEntitiesBox(this.level(), position(), 3.0)) {
            entity.deltaMovement = Vec3(entity.deltaMovement.x, 0.7, entity.deltaMovement.x)
            if (entity is ServerPlayer) {
                sendToPlayer(MovePlayerS2CPacket(Vec3(0.0, 0.7, 0.0)), entity)
            }
        }
        this.discard()
    }
}
