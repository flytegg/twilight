package gg.flyte.twilight.extension

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun Entity.getNearbyEntities(range: Double): MutableList<Entity> {
    return getNearbyEntities(range, range, range)
}

fun Entity.isOnFire(): Boolean {
    return fireTicks > 0
}