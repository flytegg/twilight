package gg.flyte.twilight.inventory

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

abstract class CustomGUI(
    title: String,
    slots: Int,
    val canMoveItems: Boolean = false
) : InventoryHolder {

    private val inventory = Bukkit.createInventory(this, slots, title)

    override fun getInventory(): Inventory {
        return inventory
    }

    abstract fun onOpen(e: InventoryOpenEvent)

    abstract fun onClick(e: InventoryClickEvent)

    abstract fun onClose(e: InventoryCloseEvent)

    fun open(player: Player) {
        player.openInventory(inventory)
    }

    fun addItem(item: ItemStack) {
        inventory.addItem(item)
    }

    fun setItem(slot: Int, item: ItemStack) {
        inventory.setItem(slot, item)
    }

    fun setItem(slots: Array<Int>, item: ItemStack) {
        slots.forEach { inventory.setItem(it, item) }
    }

}