package gg.flyte.twilight.extension

import org.bukkit.inventory.PlayerInventory

fun PlayerInventory.clearArmor() { armorContents = arrayOf(null, null, null, null) }
fun PlayerInventory.clearAll() {
    clear() // Clear Items
    clearArmor()
}