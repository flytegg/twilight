package gg.flyte.twilight.extension

import org.bukkit.inventory.Inventory

fun Inventory.hasSpace(): Boolean {
    return firstEmpty() != -1
}

fun Inventory.isFull(): Boolean {
    return firstEmpty() == -1
}