package gg.flyte.twilight.extension

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

fun LivingEntity.kill() {
    health = 0.0
}

fun LivingEntity.heal() {
    health = getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
}

fun Player.isOnLadder(): Boolean {
    return location.block.type == Material.LADDER
}

fun Player.feed() {
    foodLevel = 20
}