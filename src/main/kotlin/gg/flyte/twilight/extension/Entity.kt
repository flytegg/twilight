package gg.flyte.twilight.extension

import gg.flyte.twilight.Twilight
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

/**
 * Retrieves a list of nearby entities within the specified range from the current entity.
 *
 * This method returns a mutable list containing all entities within the specified range
 * from the current entity. The range is defined as a cubic area centered on the entity's
 * location, where `range` determines the distance from the entity to each side of the cube.
 *
 * @param range The maximum distance from the entity to each side of the cube.
 * @return A mutable list of nearby entities within the specified range.
 */
fun Entity.getNearbyEntities(range: Double): MutableList<Entity> {
    return getNearbyEntities(range, range, range)
}

/**
 * Checks if the entity is currently on fire.
 *
 * This method checks whether the entity is currently affected by the fire status effect.
 * If the entity's `fireTicks` property is greater than zero, it indicates that the entity
 * is on fire, and this method returns `true`.
 *
 * @return `true` if the entity is on fire, `false` otherwise.
 */
fun Entity.isOnFire(): Boolean {
    return fireTicks > 0
}

/**
 * Freezes the entity.
 */
fun Entity.freeze() {
    setMetadata("frozen", FixedMetadataValue(Twilight.plugin, true))
}
/**
 * Unfreezes the entity.
 */
fun Entity.unfreeze() {
    removeMetadata("frozen", Twilight.plugin)
}