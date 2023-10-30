package gg.flyte.twilight

import gg.flyte.twilight.event.event
import org.bukkit.event.player.PlayerJoinEvent

fun eventTest() {
    val listener = event<PlayerJoinEvent> {
        joinMessage = null
        player.sendMessage("Welcome!")
    }

    listener.unregister()
}