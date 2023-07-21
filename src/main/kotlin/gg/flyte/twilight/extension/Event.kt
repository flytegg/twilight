package gg.flyte.twilight.extension

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

fun InventoryClickEvent.getPlayer(): Player {
    return whoClicked as Player
}