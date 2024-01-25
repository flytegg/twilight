package gg.flyte.twilight.network.packet.inbound

import gg.flyte.twilight.network.packet.Packet
import gg.flyte.twilight.network.server.Server

open class InboundPacket(
    header: Header,
    data: ByteArray
) : Packet(header, data)

class InboundHandshakePacket(
    val self: Server
) : InboundPacket(DefaultHeader.HANDSHAKE, byteArrayOf())