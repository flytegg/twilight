package gg.flyte.twilight.event.custom.interact.listener

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.CustomTwilightListener
import gg.flyte.twilight.event.custom.interact.PlayerMainHandInteractEvent
import gg.flyte.twilight.event.custom.interact.PlayerOffHandInteractEvent
import gg.flyte.twilight.event.event
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object InteractEventListener : CustomTwilightListener() {

    init {
        events += event<PlayerInteractEvent>(ignoreCancelled = true) {
            Twilight.plugin.server.pluginManager.callEvent(
                if (hand == EquipmentSlot.HAND)
                    PlayerMainHandInteractEvent(player, action, item, clickedBlock, blockFace, clickedPosition)
                else
                    PlayerOffHandInteractEvent(player, action, item, clickedBlock, blockFace, clickedPosition)
            )
        }
    }

}