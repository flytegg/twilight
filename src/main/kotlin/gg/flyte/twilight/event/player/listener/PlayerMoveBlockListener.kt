package gg.flyte.twilight.event.player.listener

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.player.PlayerMoveBlockEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object PlayerMoveBlockListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val to = event.to ?: return
        val from = event.from

        if (from.blockX != to.blockX || from.blockY != to.blockY || from.blockZ != to.blockZ)
            Twilight.plugin.server.pluginManager.callEvent(PlayerMoveBlockEvent(event.player, from, to))
    }

}