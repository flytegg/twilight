package gg.flyte.twilight.listeners

import gg.flyte.twilight.event.event
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MoveEvent : Listener {
    init {
        event<PlayerMoveEvent> {
            if (player.hasMetadata("frozen")) isCancelled = true
        }
        event<EntityMoveEvent> {
            if (entity.hasMetadata("frozen")) isCancelled = true
        }
    }
}