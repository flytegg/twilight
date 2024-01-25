package gg.flyte.twilight.network.server

import gg.flyte.twilight.network.Network
import gg.flyte.twilight.network.packet.outbound.OutboundHandshakePacket
import java.util.*

open class Server(
    val id: UUID = UUID.randomUUID(),
    var name: String = "UNKNOWN",
) {

    fun performHandshake() {
        Network.packetHandler.send(OutboundHandshakePacket(this))
    }

    override fun toString(): String = "Server(id=$id, name='$name')"

}