package gg.flyte.twilight.outdated.inventory

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

abstract class CustomGUI(
    title: Component,
    size: Int = 27,
//    val canModify: Boolean = false,
    inventoryType: InventoryType? = null,
) : InventoryHolder {

    private val inventory = if (inventoryType != null) Bukkit.createInventory(this, inventoryType, title)
    else Bukkit.createInventory(this, size, title)

    override fun getInventory() = inventory

    fun open(player: Player) { player.openInventory(inventory) }

    abstract fun onOpen(event: InventoryOpenEvent)
    abstract fun onClick(event: InventoryClickEvent)
    abstract fun onClose(event: InventoryCloseEvent)
}