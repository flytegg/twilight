package gg.flyte.twilight.network

import gg.flyte.twilight.network.packet.PacketHandler
import gg.flyte.twilight.network.server.Server
import gg.flyte.twilight.network.server.ServerRegistry

object Network {
    init {
        println("Initializing network")
    }

    var settings = Settings(server = Server(name = "twilight-server"))
    val packetHandler: PacketHandler = settings.packetHandler

    init {
        with(settings.server()) {
            ServerRegistry.currentServer = this
            println("Server initialized: $this")
            performHandshake()
        }
        println("Network initialized")
    }

    class Settings(
        var packetHandler: PacketHandler = PacketHandler(),
        private var server: Server
    ) {
        fun server(block: Server.() -> Unit) = server.apply(block)

        fun server(): Server = server
    }
}