package gg.flyte.twilight.event.custom.movement.listener

import gg.flyte.twilight.Twilight.Companion.isPaper
import gg.flyte.twilight.event.CustomTwilightListener
import gg.flyte.twilight.event.event
import gg.flyte.twilight.extension.frozen
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.event.player.PlayerMoveEvent

object EntityMoveEventListener : CustomTwilightListener() {
    init {
        if (isPaper()) {
            event<EntityMoveEvent> {
                if (entity.frozen()) isCancelled = true
            }
        } else {
            event<PlayerMoveEvent> {
                if (player.frozen()) isCancelled = true
            }
        }
    }
}