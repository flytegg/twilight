package gg.flyte.twilight.extension

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.util.BoundingBox

/**
 * Retrieves all the blocks within the bounding box in the specified world.
 *
 * This extension function for BoundingBox iterates through all x, y, and z
 * coordinates that the bounding box encompasses and collects the corresponding
 * blocks from the world.
 *
 * Note: This function can be performance-intensive for large bounding boxes,
 * as it iterates over every single block within the bounding box.
 *
 * @param world The world from which to retrieve the blocks.
 * @return A list of Block objects within the bounding box in the given world.
 */
fun BoundingBox.getBlocks(world: World): List<Block> {
    val blocks = mutableListOf<Block>()

    val minX = minX.toInt()
    val minY = minY.toInt()
    val minZ = minZ.toInt()
    val maxX = maxX.toInt()
    val maxY = maxY.toInt()
    val maxZ = maxZ.toInt()

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                blocks.add(world.getBlockAt(x, y, z))
            }
        }
    }

    return blocks
}

/**
 * Retrieves all the locations within the bounding box in the specified world.
 *
 * This extension function for BoundingBox iterates through all x, y, and z
 * coordinates that the bounding box encompasses and collects the corresponding
 * locations from the world.
 *
 * Note: This function can be performance-intensive for large bounding boxes,
 * as it iterates over every single location within the bounding box.
 *
 * @param world The world in which the bounding box is defined.
 * @return A list of Location objects representing each block position within the bounding box.
 */
fun BoundingBox.getLocations(world: World): List<Location> {
    val locations = mutableListOf<Location>()

    val minX = minX.toInt()
    val minY = minY.toInt()
    val minZ = minZ.toInt()
    val maxX = maxX.toInt()
    val maxY = maxY.toInt()
    val maxZ = maxZ.toInt()

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                locations.add(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
            }
        }
    }

    return locations
}