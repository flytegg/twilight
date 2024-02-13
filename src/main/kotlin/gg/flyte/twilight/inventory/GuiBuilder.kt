package gg.flyte.twilight.inventory

import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType

class GuiBuilder(
    title: Component,
    size: Int = 27,
    inventoryType: InventoryType? = null,

    block: GuiBuilder.() -> Unit = {}
) : CustomGUI(title, size, inventoryType) {

    init { apply(block) }

    private var onOpen: InventoryOpenEvent.() -> Unit = {}
    private var onClick: InventoryClickEvent.() -> Unit = {}
    private var onClose: InventoryCloseEvent.() -> Unit = {}

    fun onOpen(block: InventoryOpenEvent.() -> Unit) { onOpen = block }
    fun onClick(block: InventoryClickEvent.() -> Unit) { onClick = block }
    fun onClose(block: InventoryCloseEvent.() -> Unit) { onClose = block }

    override fun onOpen(event: InventoryOpenEvent) { onOpen.invoke(event) }
    override fun onClick(event: InventoryClickEvent) { onClick.invoke(event) }
    override fun onClose(event: InventoryCloseEvent) { onClose.invoke(event) }


}