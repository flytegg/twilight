package gg.flyte.twilight

import gg.flyte.twilight.event.event
import org.bukkit.event.player.PlayerJoinEvent

fun eventTest() {
    event<PlayerJoinEvent> {
        joinMessage = null
        player.sendMessage("Welcome!")
    }
}