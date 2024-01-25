package gg.flyte.twilight.network.packet

import gg.flyte.twilight.network.server.ServerRegistry
import java.util.*

open class Packet(
    val header: Header,
    val data: ByteArray
) {

    val id: UUID = UUID.randomUUID()
    val sender: UUID = ServerRegistry.currentServer.id

    interface Header

    enum class DefaultHeader : Header {
        HANDSHAKE,
        PING,
        PONG
    }

    companion object {
        val NULL_ID = UUID(0, 0)
    }

    override fun toString(): String = "Packet(header=$header, data=$data, id=$id, sender=$sender)"
}

