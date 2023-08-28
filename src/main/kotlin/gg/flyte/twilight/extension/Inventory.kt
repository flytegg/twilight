package gg.flyte.twilight.extension

import org.bukkit.inventory.Inventory

/**
 * Checks if the inventory has available space for more items.
 *
 * This method checks if the inventory has any empty slots available for adding more items.
 * If there is at least one empty slot in the inventory, the method returns `true`, indicating
 * that there is available space. If all slots in the inventory are filled with items, the method
 * returns `false`, indicating that the inventory is full and has no more space.
 *
 * @return `true` if the inventory has available space, `false` if the inventory is full.
 */
fun Inventory.hasSpace(): Boolean {
    return firstEmpty() != -1
}

/**
 * Checks if the inventory is completely filled with items.
 *
 * This method checks if all slots in the inventory are filled with items. If all slots are occupied,
 * the method returns `true`, indicating that the inventory is full. If there is at least one empty
 * slot in the inventory, the method returns `false`, indicating that the inventory has available space.
 *
 * @return `true` if the inventory is full, `false` if there is available space.
 */
fun Inventory.isFull(): Boolean {
    return firstEmpty() == -1
}