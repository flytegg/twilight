package gg.flyte.twilight.network.packet

import gg.flyte.twilight.network.message.PacketNetworkMessage
import gg.flyte.twilight.network.server.ServerRegistry

open class PacketHandler {

    init {
        println("Packet handler initialized")
    }

    fun send(packet: Packet) {
        println("[twilight-packet-handler] ${ServerRegistry.currentServer} is sending: $packet")
        PacketNetworkMessage(packet).send()
    }

    fun handle(packet: Packet) {
        println("[twilight-packet-handler] ${ServerRegistry.currentServer} is handling: $packet")
    }

}