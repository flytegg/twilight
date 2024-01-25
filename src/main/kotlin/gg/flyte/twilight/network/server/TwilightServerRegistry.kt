package gg.flyte.twilight.network.server

import java.util.*

object TwilightServerRegistry {
    private val servers = mutableMapOf<UUID, TwilightServer>()

    fun register(server: TwilightServer) = servers.put(server.id, server)

    fun unregister(server: TwilightServer) = servers.remove(server.id, server)

    fun findById(id: UUID): TwilightServer? = servers[id]
}