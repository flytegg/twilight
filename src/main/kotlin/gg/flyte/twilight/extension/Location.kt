package gg.flyte.twilight.extension

import org.bukkit.Location
import org.bukkit.inventory.ItemStack

/**
 * Drops the specified [itemStack] at the current location in the world.
 *
 * @param itemStack The ItemStack to be dropped at the location.
 */
fun Location.dropItem(itemStack: ItemStack) {
    world!!.dropItem(this, itemStack)
}

/**
 * Drops the specified [itemStack] naturally at the current location in the world.
 * The item will be dropped with a random offset (i.e. naturally).
 *
 * @param itemStack The ItemStack to be dropped naturally at the location.
 */
fun Location.dropItemNaturally(itemStack: ItemStack) {
    world!!.dropItemNaturally(this, itemStack)
}
