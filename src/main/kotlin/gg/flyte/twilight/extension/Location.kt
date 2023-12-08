package gg.flyte.twilight.extension

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

/**
 * An extension of the Bukkit Location class, providing conversion of the given coordinates
 * to double and orientation to float.
 *
 * @param world The world in which the location exists.
 * @param x The x-coordinate of the location.
 * @param y The y-coordinate of the location.
 * @param z The z-coordinate of the location.
 * @param yaw The yaw (rotation on the horizontal plane) at this location, defaulting to 0 if not specified.
 * @param pitch The pitch (vertical rotation) at this location, defaulting to 0 if not specified.
 *
 * @constructor Creates a new Location object with specified world, coordinates, and optionally, orientation.
 */
data class Location(
    private val _world: World,
    val x: Number,
    val y: Number,
    val z: Number,
    val yaw: Number = 0,
    val pitch: Number = 0
) : Location(_world, x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())

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

/**
 * Increases the X coordinate of this location by the specified amount.
 *
 * @param x The amount to add to the X coordinate.
 * @return The modified Location object.
 */
fun Location.addX(x: Number): Location {
    return add(x.toDouble(), 0.0, 0.0)
}

/**
 * Increases the Y coordinate of this location by the specified amount.
 *
 * @param y The amount to add to the Y coordinate.
 * @return The modified Location object.
 */
fun Location.addY(y: Number): Location {
    return add(0.0, y.toDouble(), 0.0)
}

/**
 * Increases the Z coordinate of this location by the specified amount.
 *
 * @param z The amount to add to the Z coordinate.
 * @return The modified Location object.
 */
fun Location.addZ(z: Number): Location {
    return add(0.0, 0.0, z.toDouble())
}

/**
 * Increases the X, Y, and Z coordinates of this location.
 *
 * @param x The amount to add to the X coordinate (default 0).
 * @param y The amount to add to the Y coordinate (default 0).
 * @param z The amount to add to the Z coordinate (default 0).
 * @return The modified Location object.
 */
fun Location.add(x: Number = 0, y: Number = 0, z: Number = 0): Location {
    return add(x.toDouble(), y.toDouble(), z.toDouble())
}

/**
 * Decreases the X coordinate of this location by the specified amount.
 *
 * @param x The amount to subtract from the X coordinate.
 * @return The modified Location object.
 */
fun Location.subtractX(x: Number): Location {
    return subtract(x.toDouble(), 0.0, 0.0)
}

/**
 * Decreases the Y coordinate of this location by the specified amount.
 *
 * @param y The amount to subtract from the Y coordinate.
 * @return The modified Location object.
 */
fun Location.subtractY(y: Number): Location {
    return subtract(0.0, y.toDouble(), 0.0)
}

/**
 * Decreases the Z coordinate of this location by the specified amount.
 *
 * @param z The amount to subtract from the Z coordinate.
 * @return The modified Location object.
 */
fun Location.subtractZ(z: Number): Location {
    return subtract(0.0, 0.0, z.toDouble())
}

/**
 * Decreases the X, Y, and Z coordinates of this location.
 *
 * @param x The amount to subtract from the X coordinate (default 0).
 * @param y The amount to subtract from the Y coordinate (default 0).
 * @param z The amount to subtract from the Z coordinate (default 0).
 * @return The modified Location object.
 */
fun Location.subtract(x: Number = 0, y: Number = 0, z: Number = 0): Location {
    return subtract(x.toDouble(), y.toDouble(), z.toDouble())
}