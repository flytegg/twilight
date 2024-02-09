package gg.flyte.twilight.extension

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity

/**
 * Kills the living entity.
 *
 * This method sets the health of the living entity to 0, effectively killing it.
 * After calling this method, the entity will be removed from the game world if it
 * is not being kept alive by external means (e.g., respawn anchors for players).
 */
fun LivingEntity.kill() {
    health = 0.0
}

/**
 * Heals the living entity to its maximum health.
 *
 * This method restores the health of the living entity to its maximum possible value.
 * The new health is determined by the entity's `Attribute.GENERIC_MAX_HEALTH` attribute.
 * If this attribute is not available, the entity's health will remain unchanged.
 */
fun LivingEntity.heal() {
    health = getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
}

/**
 * Checks if the living entity is standing on a ladder block.
 *
 * This method checks whether the living entity is currently standing on a ladder block in the game world.
 *
 * @return `true` if the player is standing on a ladder block, `false` otherwise.
 */
fun LivingEntity.isOnLadder(): Boolean {
    return location.block.type == Material.LADDER
}

/**
 * Teleports the living entity to the specified coordinates in their current world.
 *
 * @param x The X-coordinate to teleport the player to (double or int).
 * @param y The Y-coordinate to teleport the player to (double or int).
 * @param z The Z-coordinate to teleport the player to (double or int).
 */
fun LivingEntity.teleport(x: Number, y: Number, z: Number) {
    teleport(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
}