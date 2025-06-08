package net.igneo.icv.networking

import net.igneo.icv.networking.packet.*
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.Supplier

object ModMessages {
    private var INSTANCE: SimpleChannel? = null
    private var packetId = 0

    private fun id(): Int {
        return packetId++
    }

    fun register() {
        val net = ChannelBuilder.named(ResourceLocation("icv", "messages")).networkProtocolVersion { "1.0" }
            .clientAcceptedVersions { _: String? -> true }
            .serverAcceptedVersions { _: String? -> true }.simpleChannel()
        INSTANCE = net
        net.messageBuilder(EnchantUseC2SPacket::class.java, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(::EnchantUseC2SPacket)
            .encoder(EnchantUseC2SPacket::toBytes)
            .consumerMainThread(EnchantUseC2SPacket::handle)
            .add()
        net.messageBuilder(InputSyncC2SPacket::class.java, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(::InputSyncC2SPacket)
            .encoder(InputSyncC2SPacket::toBytes)
            .consumerMainThread(InputSyncC2SPacket::handle)
            .add()
        net.messageBuilder(AnimatedSyncC2SPacket::class.java, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(::AnimatedSyncC2SPacket)
            .encoder(AnimatedSyncC2SPacket::toBytes)
            .consumerMainThread(AnimatedSyncC2SPacket::handle)
            .add()
        net.messageBuilder(EquipmentUpdateS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(::EquipmentUpdateS2CPacket)
            .encoder(EquipmentUpdateS2CPacket::toBytes)
            .consumerMainThread(EquipmentUpdateS2CPacket::handle)
            .add()
        net.messageBuilder(EntitySyncS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(::EntitySyncS2CPacket)
            .encoder(EntitySyncS2CPacket::toBytes)
            .consumerMainThread(EntitySyncS2CPacket::handle)
            .add()
        net.messageBuilder(EnchantAttackS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(::EnchantAttackS2CPacket)
            .encoder(EnchantAttackS2CPacket::toBytes)
            .consumerMainThread(EnchantAttackS2CPacket::handle)
            .add()
        net.messageBuilder(EnchantHitS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder { buf: FriendlyByteBuf? -> EnchantHitS2CPacket(buf!!) }
            .encoder { obj: EnchantHitS2CPacket, buf: FriendlyByteBuf? -> obj.toBytes(buf!!) }
            .consumerMainThread { obj: EnchantHitS2CPacket, supplier: Supplier<NetworkEvent.Context?>? -> obj.handle( supplier ) }
            .add()
        net.messageBuilder(MovePlayerS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(::MovePlayerS2CPacket)
            .encoder(MovePlayerS2CPacket::toBytes)
            .consumerMainThread(MovePlayerS2CPacket::handle)
            .add()
        net.messageBuilder(PushPlayerS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(::PushPlayerS2CPacket)
            .encoder(PushPlayerS2CPacket::toBytes)
            .consumerMainThread(PushPlayerS2CPacket::handle)
            .add()
        net.messageBuilder(SendBlinkShaderS2CPacket::class.java, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(::SendBlinkShaderS2CPacket)
            .encoder(SendBlinkShaderS2CPacket::toBytes)
            .consumerMainThread(SendBlinkShaderS2CPacket::handle)
            .add()
    }

    fun <MSG> sendToServer(message: MSG) {
        INSTANCE!!.sendToServer(message)
    }

    fun <MSG> sendToPlayer(message: MSG, player: ServerPlayer?) {
        INSTANCE!!.send(PacketDistributor.PLAYER.with { player }, message)
    }
}
