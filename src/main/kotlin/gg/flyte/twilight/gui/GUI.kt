package gg.flyte.twilight.gui

import gg.flyte.twilight.event.event
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

inline fun gui(
    title: Component = Component.text("Custom GUI"),
    size: Int = 27,
    type: InventoryType = InventoryType.CHEST,
    noinline context: GUI.() -> Unit
): GUI {
    return GUI(title, size, type, context)
}

class GUI(val title: Component, val size: Int, val type: InventoryType, val context: GUI.() -> Unit) {

    private val inventory = when (type) {
        InventoryType.CHEST -> Bukkit.createInventory(null, size, title)
        else -> Bukkit.createInventory(null, type, title)
    }

    private val keySlot = mutableMapOf<Char, MutableList<Int>>()
    private val slotAction = mutableMapOf<Int, InventoryClickEvent.() -> Unit>()

    lateinit var viewer: Player

    private val clickEvent = event<InventoryClickEvent> {
        if (inventory == this@GUI.inventory) slotAction[-1]?.invoke(this)
        if (clickedInventory != this@GUI.inventory) return@event
        slotAction[slot]?.invoke(this)
    }

    fun pattern(vararg pattern: String) {
        for ((index, value) in pattern.joinToString("").withIndex()) {
            keySlot.getOrPut(value) { mutableListOf() }.add(index)
        }
    }

    /**
     * Set the action to be executed when the player clicks on any slot while the GUI is open.
     */
    fun onClick(action: InventoryClickEvent.() -> Unit) {
        slotAction[-1] = action
    }

    @JvmName("setSlot")
    fun set(slot: Int, item: ItemStack, action: InventoryClickEvent.() -> Unit = {}) {
        inventory.setItem(slot, item)
        slotAction[slot] = action
    }

    @JvmName("setSlots")
    fun set(indexes: Collection<Int>, item: ItemStack, action: InventoryClickEvent.() -> Unit = {}) {
        indexes.forEach { set(it, item, action) }
    }

    @JvmName("setKey")
    fun set(key: Char, item: ItemStack, action: InventoryClickEvent.() -> Unit = {}) {
        keySlot[key]?.forEach { set(it, item, action) }
    }

    @JvmName("setKeys")
    fun set(keys: Collection<Char>, item: ItemStack, action: InventoryClickEvent.() -> Unit = {}) {
        keys.forEach { set(it, item, action) }
    }

    private fun remove() {
        keySlot.clear()
        slotAction.clear()
        inventory.clear()
        clickEvent.unregister()
    }

    companion object {
        fun Player.openInventory(gui: GUI) {
            gui.viewer = this
            gui.context.invoke(gui)
            openInventory(gui.inventory)
        }
    }

}