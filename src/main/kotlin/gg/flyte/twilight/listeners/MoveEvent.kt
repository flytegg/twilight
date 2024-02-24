package gg.flyte.twilight.listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MoveEvent : Listener {
    @EventHandler
    fun onJump(event: PlayerJumpEvent) {
        val player = event.player
        if (player.hasMetadata("frozen")) event.isCancelled = true
    }

}