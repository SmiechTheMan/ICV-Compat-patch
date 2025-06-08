package net.igneo.icv.entity.chestplate.soulOrb

import net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate.HauntManager
import net.igneo.icv.entity.ICVEntity
import net.igneo.icv.entity.ModEntities
import net.igneo.icv.entity.chestplate.soulSpider.SoulSpiderEntity
import net.igneo.icv.init.ICVUtils.getManagerForType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class SoulOrbEntity(pEntityType: EntityType<out Projectile?>?, pLevel: Level?) :
    ICVEntity(pEntityType!!, pLevel!!) {
    private var child: SoulSpiderEntity? = null
    private var lifeTime: Int = 0

    override val gravity: Double
        get() = 0.0

    override val airFriction: Double
        get() = 0.2

    override fun tick() {
        super.tick()
        if (lifeTime > 200) {
            this.discard()
        } else {
            ++lifeTime
        }
        if (child == null) {
            child = ModEntities.SOUL_SPIDER.get().create(this.level())
            child!!.owner = this.owner
            child!!.setPos(this.position())
            child!!.setOrb(this)
            level().addFreshEntity(child)
        }
    }

    override fun remove(pReason: RemovalReason) {
        child!!.discard()
        val manager = (getManagerForType(
            (this.owner as Player?)!!,
            HauntManager::class.java
        ) as HauntManager?)
        if (manager != null) manager.child = null
        super.remove(pReason)
    }
}
