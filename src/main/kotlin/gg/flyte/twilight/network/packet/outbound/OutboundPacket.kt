package gg.flyte.twilight.network.packet.outbound

import gg.flyte.twilight.network.packet.Packet
import gg.flyte.twilight.network.server.Server

open class OutboundPacket(
    header: Header,
    data: ByteArray
) : Packet(header, data)

class OutboundHandshakePacket(
    val self: Server
) : OutboundPacket(DefaultHeader.HANDSHAKE, byteArrayOf())

