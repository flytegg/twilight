package gg.flyte.twilight.extension

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

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
 * Checks if the player is standing on a ladder block.
 *
 * This method checks whether the player is currently standing on a ladder block in the game world.
 * If the player's location is on a ladder block, this method returns `true`; otherwise, it returns `false`.
 *
 * @return `true` if the player is standing on a ladder block, `false` otherwise.
 */
fun Player.isOnLadder(): Boolean {
    return location.block.type == Material.LADDER
}

/**
 * Restores the player's food level to maximum.
 *
 * This method sets the player's food level to its maximum value of 20, effectively
 * filling the player's hunger bar completely. The player will be fully fed after
 * calling this method.
 */
fun Player.feed() {
    foodLevel = 20
}