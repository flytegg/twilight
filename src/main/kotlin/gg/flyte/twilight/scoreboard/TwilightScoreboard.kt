package gg.flyte.twilight.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

class TwilightScoreboard(private val plugin: JavaPlugin) {
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private var objective = scoreboard.registerNewObjective("sidebar", "dummy")

    private var updateTask: BukkitTask? = null

    private data class LineUpdate(
        val initialLine: Component,
        val updateFunction: () -> Component
    )

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    /**
     * @param title the displayname of the scoreboard
     * Used for setting the displayname of a scoreboard
     */
    fun title(title: Component) {
        objective.displayName(title)
    }

    /**
     * @param text the component you want to use at that line
     * @param score the score you want to set
     * Used to change a specific line.
     */
    fun set(text: Component, score: Int) {
        scoreboard.entries.filter {
            objective.getScore(it).score == score
        }.forEach {
            scoreboard.resetScores(it)
        }

        val uniqueEntry = " ".repeat(score)
        val team = scoreboard.registerNewTeam(uniqueEntry)
        team.prefix(text)
        team.addEntry(uniqueEntry)

        objective.getScore(uniqueEntry).score = score
    }
    /**
     * @param lines actual components that you want in your scoreboard
     * Used to add how many lines you want to the scoreboard
     */
    fun setAll(vararg lines: Component) {
        clearLines()
        lines.withIndex().forEach { (index, line) ->
            val uniqueEntry = "§${lines.size - index}"
            val team = scoreboard.registerNewTeam(uniqueEntry)
            team.prefix(line)
            team.addEntry(uniqueEntry)
            objective.getScore(uniqueEntry).score = lines.size - index
        }
    }

    /**
     * @param score the number of the line you want to get.
     * Return a component at a specific line.
     */
    fun get(score: Int): Component? {
        return scoreboard.entries.find {
            objective.getScore(it).score == score
        }?.let { entry ->
            scoreboard.getTeam(entry)?.prefix()
        }
    }

    /**
     * @param score the line you want to remove.
     * Remove a specific line.
     */
    fun remove(score: Int) {
        scoreboard.entries.find {
            objective.getScore(it).score == score
        }?.let {
            scoreboard.resetScores(it)
        }
    }

    /**
     * Clear lines from the scoreboard
     */
    private fun clearLines() {
        scoreboard.entries.filter {
            objective.getScore(it).score > 0
        }.forEach {
            scoreboard.resetScores(it)
        }
    }

    /**
     * Remove every objective of the score
     */
    fun delete() {
        updateTask?.cancel()
        updateTask = null
        scoreboard.objectives.forEach { it.unregister() }
    }


    /**
     * @param player the player you want to assign the scoreboard to
     * Assign the scoreboard to a player (Use a for Loop if you want to assign this to multiple players)
     */
    fun assignTo(player: Player) {
        player.scoreboard = scoreboard
    }

    /**
     * @param player the player you want to remove the scoreboard to
     * Assign the scoreboard to a player (Use a for Loop if you want to assign this to multiple players)
     */
    fun removeTo(player: Player) {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }

    /**
     * @param updateInterval the refresh rate of the scoreboard (In Ticks)
     * @param lines dynamic components to display, with null values preserving existing lines
     * Used to create dynamic scoreboard with partial updates
     */
    fun updateLines(updateInterval: Long, vararg lines: Component?) {
        updateTask?.cancel()

        val currentLines = lines.mapIndexed { index, line ->
            line ?: get(lines.size - index) ?: Component.empty()
        }

        updateTask = object : BukkitRunnable() {
            override fun run() {
                clearLines()
                currentLines.withIndex().forEach { (index, line) ->
                    val uniqueEntry = "§${currentLines.size - index}"
                    scoreboard.getTeam(uniqueEntry)?.unregister()
                    val team = scoreboard.registerNewTeam(uniqueEntry)
                    team.prefix(line)
                    team.addEntry(uniqueEntry)
                    objective.getScore(uniqueEntry).score = currentLines.size - index
                }
            }
        }.runTaskTimer(plugin, 0L, updateInterval)
    }
}