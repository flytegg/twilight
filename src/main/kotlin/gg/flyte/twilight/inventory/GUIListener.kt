package gg.flyte.twilight.inventory

import gg.flyte.twilight.event.CustomTwilightListener
import gg.flyte.twilight.event.event
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory

object GUIListener : CustomTwilightListener() {

    init {
        events += event<InventoryClickEvent>(EventPriority.NORMAL, ignoreCancelled = true) {
            GuiManager.slotActions[inventory]?.get(slot)?.invoke(this)
        }

        events += event<InventoryOpenEvent>(EventPriority.NORMAL, ignoreCancelled = true) {
            if (inventory.isNotCustom()) return@event
            (inventory.holder as CustomGUI).onOpen(this)
        }

        events += event<InventoryClickEvent>(EventPriority.NORMAL, ignoreCancelled = true) {
            if (inventory.isNotCustom() || view.topInventory != inventory) return@event
            (inventory.holder as CustomGUI).onClick(this)
        }

        events += event<InventoryCloseEvent>(EventPriority.NORMAL, ignoreCancelled = true) {
            if (inventory.isNotCustom()) return@event
            (inventory.holder as CustomGUI).onClose(this)
        }
    }

    private fun Inventory.isNotCustom(): Boolean = holder == null || holder !is CustomGUI

}