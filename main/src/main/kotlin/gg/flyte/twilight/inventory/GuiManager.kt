package gg.flyte.twilight.inventory

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object GuiManager {

    val slotActions = mutableMapOf<Inventory, HashMap<Int, InventoryClickEvent.() -> Unit>>()

}