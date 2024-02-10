package gg.flyte.twilight.extension

import org.bukkit.Material
import java.util.regex.Pattern

private val SLAB_PATTERN: Pattern = Pattern.compile("^.*SLAB$")

/**
 * Extension function to check whether a FallingBlock landing on this Material will break and drop.
 *
 * @return `true` if the FallingBlock will break and drop, `false` otherwise.
 */
fun Material.isBreakingFallingBlock(): Boolean {
    return this.isTransparent &&
            this != Material.NETHER_PORTAL &&
            this != Material.END_PORTAL ||
            this == Material.COBWEB ||
            this == Material.DAYLIGHT_DETECTOR ||
            isTrapDoor() ||
            this == Material.OAK_SIGN ||
            isWallSign() ||
            SLAB_PATTERN.matcher(this.name).matches()
}

/**
 * Check whether the Material is a tool.
 *
 * @return `true` if the Material is a tool, `false` otherwise.
 */
fun Material.isTool(): Boolean {
    return name.endsWith("AXE") ||
            name.endsWith("SPADE") ||
            name.endsWith("SWORD") ||
            name.endsWith("HOE") ||
            name.endsWith("BUCKET") ||
            this == Material.BOW ||
            this == Material.FISHING_ROD ||
            this == Material.CLOCK ||
            this == Material.COMPASS ||
            this == Material.FLINT_AND_STEEL
}

/**
 * Check whether the Material is an armor item.
 *
 * @return `true` if the Material is an armor, `false` otherwise.
 */
fun Material.isArmor(): Boolean {
    return name.endsWith("HELMET") ||
            name.endsWith("CHESTPLATE") ||
            name.endsWith("LEGGINGS") ||
            name.endsWith("BOOTS")
}

/**
 * Check whether the Material is a skull.
 *
 * @return `true` if the Material is a skull, `false` otherwise.
 */
fun Material.isSkull(): Boolean {
    val name = toString()
    return (name.endsWith("_HEAD") || name.endsWith("_SKULL")) && !name.contains("WALL")
}

/**
 * Check whether the Material is a trap door (any variation).
 *
 * @return `true` if the Material is a trap door, `false` otherwise.
 */
fun Material.isTrapDoor(): Boolean {
    val name = toString()
    return name.contains("TRAP_DOOR") || name.contains("TRAPDOOR")
}

/**
 * Check whether the Material is a wall sign (any variation).
 *
 * @return `true` if the Material is a wall sign, `false` otherwise.
 */
fun Material.isWallSign(): Boolean {
    return name.contains("WALL_SIGN")
}