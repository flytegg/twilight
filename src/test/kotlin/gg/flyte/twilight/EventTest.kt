package gg.flyte.twilight

import gg.flyte.twilight.event.custom.admin.listener.OpEventListener
import gg.flyte.twilight.event.disableCustomEventListeners
import gg.flyte.twilight.event.event
import org.bukkit.event.player.PlayerJoinEvent

fun eventTest() {
    val listener = event<PlayerJoinEvent> {
        joinMessage = null
        player.sendMessage("Welcome!")
    }

    listener.unregister()

    disableCustomEventListeners(OpEventListener)
}