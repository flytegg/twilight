package gg.flyte.twilight.extension

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

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
 * Gets the nearest player to this entity.
 * @return The nearest player.
 */

fun Entity.getNearestPlayer(): Player? {
    var nearestDistance = Double.MAX_VALUE
    var nearestPlayer: Player? = null
    for (player in world.players) {
        if (player == this) continue
        val distance = location.distance(player.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestPlayer = player
        }
    }
    return nearestPlayer
}


/**
 * Gets the nearest player to this entity within the given radius.
 * @param radius The radius to search.
 * @return The nearest player.
 */

fun Entity.getNearestPlayer(radius: Double): Player? {
    var nearestDistance = Double.MAX_VALUE
    var nearestPlayer: Player? = null
    for (player in world.getNearbyPlayers(location, radius)) {
        if (player == this) continue
        val distance = location.distance(player.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestPlayer = player
        }
    }
    return nearestPlayer
}


/**
 * Gets the nearest player to this entity within the given radius.
 * @param xRadius The x radius to search.
 * @param yRadius The y radius to search.
 * @param zRadius The z radius to search.
 * @return The nearest player.
 */

fun Entity.getNearestPlayer(xRadius: Double, yRadius: Double, zRadius: Double): Player? {
    var nearestDistance = Double.MAX_VALUE
    var nearestPlayer: Player? = null
    for (player in world.getNearbyPlayers(location, xRadius, yRadius, zRadius)) {
        if (player == this) continue
        val distance = location.distance(player.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestPlayer = player
        }
    }
    return nearestPlayer
}


/**
 * Gets the nearest entity to this entity.
 * @return The nearest entity.
 */

fun Entity.getNearestEntity(): Entity? {
    var nearestDistance = Double.MAX_VALUE
    var nearestEntity: Entity? = null
    for (entity in world.entities.also { it.remove(this) }) {
        if (entity == this) continue
        val distance = location.distance(entity.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestEntity = entity
        }
    }
    return nearestEntity
}


/**
 * Gets the nearest entity to this entity within the given radius.
 * @param radius The radius to search.
 * @return The nearest entity.
 */

fun Entity.getNearestEntity(radius: Double): Entity? {
    var nearestDistance = Double.MAX_VALUE
    var nearestEntity: Entity? = null
    for (entity in world.getNearbyEntities(location, radius, radius, radius)) {
        if (entity == this) continue
        val distance = location.distance(entity.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestEntity = entity
        }
    }
    return nearestEntity
}


/**
 * Gets the nearest entity to this entity within the given radius.
 * @param xRadius The x radius to search.
 * @param yRadius The y radius to search.
 * @param zRadius The z radius to search.
 * @return The nearest entity.
 */

fun Entity.getNearestEntity(xRadius: Double, yRadius: Double, zRadius: Double): Entity? {
    var nearestDistance = Double.MAX_VALUE
    var nearestEntity: Entity? = null
    for (entity in world.getNearbyEntities(location, xRadius, yRadius, zRadius)) {
        if (entity == this) continue
        val distance = location.distance(entity.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestEntity = entity
        }
    }
    return nearestEntity
}


/**
 * Gets the nearest living entity to this entity.
 * @return The nearest living entity.
 */

fun Entity.getNearestLivingEntity(): LivingEntity? {
    var nearestDistance = Double.MAX_VALUE
    var nearestEntity: LivingEntity? = null
    for (entity in world.livingEntities) {
        if (entity == this) continue
        val distance = location.distance(entity.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestEntity = entity
        }
    }
    return nearestEntity
}


/**
 * Gets the nearest living entity to this entity within the given radius.
 * @param radius The radius to search.
 * @return The nearest living entity.
 */

fun Entity.getNearestLivingEntity(radius: Double): LivingEntity? {
    var nearestDistance = Double.MAX_VALUE
    var nearestEntity: LivingEntity? = null
    for (entity in world.getNearbyLivingEntities(location, radius)) {
        if (entity == this) continue
        val distance = location.distance(entity.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestEntity = entity
        }
    }
    return nearestEntity
}


/**
 * Gets the nearest living entity to this entity within the given radius.
 * @param xRadius The x radius to search.
 * @param yRadius The y radius to search.
 * @param zRadius The z radius to search.
 * @return The nearest living entity.
 */

fun Entity.getNearestLivingEntity(xRadius: Double, yRadius: Double, zRadius: Double): LivingEntity? {
    var nearestDistance = Double.MAX_VALUE
    var nearestEntity: LivingEntity? = null
    for (entity in world.getNearbyLivingEntities(location, xRadius, yRadius, zRadius)) {
        if (entity == this) continue
        val distance = location.distance(entity.location)
        if (distance < nearestDistance) {
            nearestDistance = distance
            nearestEntity = entity
        }
    }
    return nearestEntity
}

