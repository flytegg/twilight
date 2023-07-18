package gg.flyte.twilight.extension

import org.bukkit.entity.Entity

fun Entity.getNearbyEntities(range: Double): MutableList<Entity> {
    return getNearbyEntities(range, range, range)
}

fun Entity.isOnFire(): Boolean {
    return fireTicks > 0
}