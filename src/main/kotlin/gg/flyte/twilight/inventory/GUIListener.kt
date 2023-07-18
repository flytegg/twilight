package gg.flyte.twilight.inventory

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory

class GUIListener : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    fun onInventoryOpen(e: InventoryOpenEvent) {
        if (!isCustomUI(e.inventory)) return

        (e.inventory.holder as CustomGUI).onOpen(e)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    fun onInventoryClick(e: InventoryClickEvent) {
        if (!isCustomUI(e.inventory)) return

        if (e.view.topInventory == e.inventory) {
            e.isCancelled = !(e.inventory.holder as CustomGUI).canMoveItems
            (e.inventory.holder as CustomGUI).onClick(e)
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    fun onInventoryClose(e: InventoryCloseEvent) {
        if (!isCustomUI(e.inventory)) return

        (e.inventory.holder as CustomGUI).onClose(e)
    }

    private fun isCustomUI(inventory: Inventory): Boolean {
        return inventory.holder != null && inventory.holder is CustomGUI
    }

}