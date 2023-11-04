package gg.flyte.twilight.extension

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
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

/**
 * Extension function for the Location class to spawn an entity of the specified type at the location.
 *
 * @param type The type of entity to spawn at this location.
 * @return The entity that was spawned.
 */
fun Location.spawnEntity(type: EntityType): Entity {
    return world!!.spawnEntity(this, type)
}