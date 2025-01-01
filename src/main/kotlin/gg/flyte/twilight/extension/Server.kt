package gg.flyte.twilight.extension

import gg.flyte.twilight.server.ServerSoftware
import org.bukkit.Server

fun Server.isFolia(): Boolean = ServerSoftware.isFolia()
fun Server.isPaper(): Boolean = ServerSoftware.isPaper()
fun Server.isSpigot(): Boolean = ServerSoftware.isSpigot()
fun Server.isCraftBukkit(): Boolean = ServerSoftware.isCraftBukkit()