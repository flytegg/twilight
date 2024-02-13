package gg.flyte.twilight.extension

import gg.flyte.twilight.inventory.GuiManager.slotActions
import gg.flyte.twilight.inventory.InventoryCorner
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

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
fun Inventory.hasSpace() = firstEmpty() != -1


/**
 * Checks if the inventory is completely filled with items.
 *
 * This method checks if all slots in the inventory are filled with items. If all slots are occupied,
 * the method returns `true`, indicating that the inventory is full. If there is at least one empty
 * slot in the inventory, the method returns `false`, indicating that the inventory has available space.
 *
 * @return `true` if the inventory is full, `false` if there is available space.
 */
fun Inventory.isFull() = firstEmpty() == -1


fun Inventory.addItems(vararg items: ItemStack) { items.forEach { addItem(it) } }

fun Inventory.slot(
    slot: Int,
    item: ItemStack? = null,
    block: InventoryClickEvent.() -> Unit = {}
) {
    if (item != null) setItem(slot, item)

    slotActions
        .getOrPut(this) { HashMap() }
        .getOrPut(slot) { block }
}

fun Inventory.setSlot(
    slot: Int,
    item: ItemStack,
    block: InventoryClickEvent.() -> Unit = {}
) { slot(slot, item, block) }

fun Inventory.setSlots(item: ItemStack, slots: Array<Int>) { slots.forEach { setItem(it, item) } }
fun Inventory.setSlots(item: ItemStack, vararg slots: Int) { setSlots(item, slots.toTypedArray()) }
fun Inventory.setSlots(item: ItemStack, slots: Iterable<Int>) { slots.forEach { setItem(it, item) } }

fun Inventory.setRow(item: ItemStack, row: Int) { setSlots(item, row * getRowSize() until (row + 1) * getRowSize()) }
fun Inventory.setRows(item: ItemStack, vararg rows: Int) { rows.forEach { setRow(item, it) } }
fun Inventory.getRowSize() = when (type) {
    InventoryType.CHEST -> 9
    InventoryType.DISPENSER -> 3
    InventoryType.DROPPER -> 3
    InventoryType.BREWING -> 3
    InventoryType.ENDER_CHEST -> 9
    InventoryType.HOPPER -> 5
    InventoryType.SHULKER_BOX -> 9
    InventoryType.BARREL -> 9
    InventoryType.WORKBENCH -> 3
    InventoryType.FURNACE -> 3
    InventoryType.CRAFTING -> 2
    InventoryType.ANVIL -> 3
    InventoryType.ENCHANTING -> 2
    InventoryType.BEACON -> 1
    InventoryType.MERCHANT -> 3
    InventoryType.CREATIVE -> 9
    InventoryType.PLAYER -> 9
    InventoryType.CARTOGRAPHY -> 3
    InventoryType.GRINDSTONE -> 3
    InventoryType.LECTERN -> 3
    InventoryType.LOOM -> 3
    InventoryType.STONECUTTER -> 3
    InventoryType.SMOKER -> 3
    InventoryType.BLAST_FURNACE -> 3
    else -> 9
}
fun Inventory.getRowCount() = size / getRowSize()

fun Inventory.setColumn(item: ItemStack, column: Int) { setSlots(item,column until size step getRowSize()) }
fun Inventory.setColumns(item: ItemStack, vararg columns: Int) { columns.forEach { setColumn(item, it) } }

fun Inventory.setBorder(item: ItemStack) {
    setRow(item, 0)
    setColumn(item, 0)
    setColumn(item, 8)
    setRow(item, getRowCount() - 1)
}

fun Inventory.setEmpty(item: ItemStack) {
    contents.forEachIndexed { index, itemStack -> if (itemStack == null) setItem(index, item) }
}

fun Inventory.getCornerSlot(corner: InventoryCorner) = when (corner) {
    InventoryCorner.TOP_LEFT -> 0
    InventoryCorner.TOP_RIGHT -> getRowSize() - 1
    InventoryCorner.BOTTOM_LEFT -> size - getRowSize()
    InventoryCorner.BOTTOM_RIGHT -> size - 1
}

fun Inventory.getCorner(corner: InventoryCorner) = getItem(getCornerSlot(corner))

fun Inventory.setCorner(item: ItemStack, corner: InventoryCorner) { setItem(getCornerSlot(corner), item) }