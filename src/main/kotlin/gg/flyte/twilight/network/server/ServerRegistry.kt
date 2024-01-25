package gg.flyte.twilight.network.server

import java.util.*

object ServerRegistry {

    init {
        println("Initializing server registry")
    }

    private val servers = mutableMapOf<UUID, Server>()
    lateinit var currentServer: Server

    init {
        println("Server registry initialized")
    }

    fun register(server: Server) = servers.put(server.id, server)

    fun unregister(server: Server) = servers.remove(server.id, server)

    fun findById(id: UUID): Server? = servers[id]

}