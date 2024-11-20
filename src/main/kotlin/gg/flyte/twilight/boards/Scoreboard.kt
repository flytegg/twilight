package gg.flyte.twilight.boards

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

/**
 * A flexible and dynamic scoreboard management system for Minecraft Bukkit/Spigot servers.
 * Provides advanced features for creating, updating, and manipulating scoreboards.
 *
 * @property plugin The JavaPlugin instance used for scheduling tasks
 */
class Scoreboard(private val plugin: JavaPlugin) {
    // Core Bukkit scoreboard components
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective = scoreboard.registerNewObjective("sidebar", "dummy")

    // Tracks lines that require dynamic updates
    private val updatableLines = mutableMapOf<Int, LineUpdate>()

    // data class to manage line updates
    private data class LineUpdate(
        val initialLine: Component,
        val updateFunction: () -> Component
    )

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    /**
     * Sets the title of the scoreboard
     * @param title The title component to display
     */
    fun setName(title: Component) {
        objective.displayName(title)
    }

    /**
     * Sets a specific line at a given score position
     * @param text The text component to display
     * @param score The vertical position of the line (higher number = higher on the board)
     */
    fun set(text: Component, score: Int) {
        scoreboard.entries.find { objective.getScore(it).score == score }?.let {
            scoreboard.resetScores(it)
        }

        // Creating an unique entry for each team
        val entry = "§${score}_${text.toString().hashCode()}"

        val team = scoreboard.registerNewTeam("line_$score")
        team.prefix(text)
        team.addEntry(entry)

        objective.getScore(entry).score = score
    }

    /**
     * Sets all lines, clearing previous lines first
     * @param lines Variable number of line components
     */
    fun setAll(vararg lines: Component) {
        scoreboard.entries.filter {
            objective.getScore(it).score > 0
        }.forEach {
            scoreboard.resetScores(it)
        }

        // Set new lines, inverting index to display correctly
        lines.withIndex().forEach { (index, line) ->
            set(line, lines.size - index)
        }
    }

    /**
     * Retrieves the text of a line at a specific score
     * @param score The position to retrieve
     * @return The text at the given score, or null if not found
     */
    fun get(score: Int): Component? {
        return scoreboard.entries.find {
            objective.getScore(it).score == score
        }?.let { entry ->
            scoreboard.getTeam(entry)?.prefix()
        }
    }

    /**
     * Removes a specific line
     * @param score The position of the line to remove
     */
    fun remove(score: Int) {
        scoreboard.entries.find {
            objective.getScore(it).score == score
        }?.let {
            scoreboard.resetScores(it)
        }
    }

    /**
     * Completely clears all lines from the scoreboard
     */
    fun clear() {
        scoreboard.entries.forEach {
            scoreboard.resetScores(it)
        }
    }

    /**
     * Deletes the entire scoreboard by unregistering all objectives
     */
    fun delete() {
        scoreboard.objectives.forEach { it.unregister() }
    }

    /**
     * Assigns this scoreboard to a specific player
     * @param player The player to assign the scoreboard to
     */
    fun assignTo(player: Player) {
        player.scoreboard = scoreboard
    }

    /**
     * Dynamically updates one or more lines at specified intervals
     * @param updateInterval Interval in ticks between updates
     * @param lineUpdates Pairs of (score, update function)
     */
    fun updateLines(updateInterval: Long, vararg lineUpdates: Pair<Int, () -> Component>) {
        // Remove previous updates for these scores
        lineUpdates.forEach { (score, updateFunc) ->
            updatableLines[score] = LineUpdate(
                get(score) ?: Component.empty(),
                updateFunc
            )
        }

        object : BukkitRunnable() {
            override fun run() {
                // Update only registered lines
                updatableLines.forEach { (score, lineUpdate) ->
                    val newComponent = lineUpdate.updateFunction()
                    set(newComponent, score)
                }
            }
        }.runTaskTimer(plugin, 0L, updateInterval)
    }

    companion object {
        /**
         * Creates a new TwilightScoreboard instance
         * @param plugin The JavaPlugin instance
         * @return A new TwilightScoreboard
         */
        fun create(plugin: JavaPlugin): gg.flyte.twilight.boards.Scoreboard {
            return Scoreboard(plugin)
        }
    }
}
