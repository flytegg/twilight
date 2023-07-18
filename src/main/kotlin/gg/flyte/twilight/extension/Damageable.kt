package gg.flyte.twilight.extension

import org.bukkit.entity.Damageable
import org.bukkit.entity.Player

fun Damageable.kill() {
    health = 0.0
}

fun Damageable.heal() {
    health = maxHealth
}

fun Player.feed() {
    foodLevel = 20
}