package gg.flyte.twilight.events

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.events.custom.PlayerMainHandInteractEvent
import gg.flyte.twilight.events.custom.PlayerOffHandInteractEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class EventListener : Listener {

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val customEvent = if (e.hand == EquipmentSlot.HAND) {
            PlayerMainHandInteractEvent(
                e.player,
                e.action,
                e.item,
                e.clickedBlock,
                e.blockFace
            )
        } else {
            PlayerOffHandInteractEvent(
                e.player,
                e.action,
                e.item,
                e.clickedBlock,
                e.blockFace
            )
        }
        Twilight.plugin.server.pluginManager.callEvent(customEvent)
    }

}