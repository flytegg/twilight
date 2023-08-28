package gg.flyte.twilight.extension

import org.bukkit.scoreboard.Scoreboard

/**
 * Registers a new objective with the specified name.
 *
 * @param name The name of the new objective.
 */
fun Scoreboard.registerNewObjective(name: String) {
    registerNewObjective(name, "dummy")
}