package gg.flyte.twilight.network.message

import gg.flyte.twilight.network.packet.Packet
import org.bukkit.entity.Player

open class NetworkMessage(
    val subChannel: SubChannel,
    val data: ByteArray
) {
    fun send(player: Player) {
        println("[twilight-network-message] Sending $this to $player")
    }

    enum class SubChannel(
        val value: String
    ) {
        FORWARD_TO_PLAYER("ForwardToPlayer")
    }

    override fun toString(): String {
        return "NetworkMessage(subChannel=$subChannel, data=${data})"
    }
}

open class PacketNetworkMessage(val packet: Packet) : NetworkMessage(SubChannel.FORWARD_TO_PLAYER, packet.data) {
    fun send() {
        send(packet.sender)
        println("[twilight-network-message] Sending $this to $player")
    }
}