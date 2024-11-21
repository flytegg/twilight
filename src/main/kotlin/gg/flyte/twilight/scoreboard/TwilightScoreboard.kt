package gg.flyte.twilight.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

/**
 * @param plugin Instance of a class that extends JavaPlugin
 */

class TwilightScoreboard(private val plugin: JavaPlugin) {
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective = scoreboard.registerNewObjective("sidebar", "dummy")

    private val updatableLines = mutableMapOf<Int, LineUpdate>()

    private data class LineUpdate(
        val initialLine: Component,
        val updateFunction: () -> Component
    )

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    /* Set the title of the scoreboard */
    fun setName(title:Component) {
        objective.displayName(title)
    }

    /* Used for setting specific lines */
    fun set(text:Component, score:Int) {
        scoreboard.getTeam(" ".repeat(score))?.unregister()
        val team = scoreboard.registerNewTeam(" ".repeat(score))
        team.prefix(text)
        team.addEntry(" ".repeat(score))

        objective.getScore(" ".repeat(score)).score = score
    }

    /**
     * @param lines:Component actual components that you want in your scoreboard
    */
    fun setAll(vararg lines:Component) {
        scoreboard.entries.filter {
            objective.getScore(it).score > 0
        }.forEach {
            scoreboard.resetScores(it)
        }

        lines.withIndex().forEach { (index, line) ->
            val uniqueEntry = "§${lines.size - index}"
            val team = scoreboard.registerNewTeam(uniqueEntry)
            team.prefix(line)
            team.addEntry(uniqueEntry)
            objective.getScore(uniqueEntry).score = lines.size - index
        }
    }

    /* get a specific line */
    fun get(score: Int): Component? {
        return scoreboard.entries.find {
            objective.getScore(it).score == score
        }?.let { entry ->
            scoreboard.getTeam(entry)?.prefix()
        }
    }

    /* remove a specific line */
    fun remove(score: Int) {
        scoreboard.entries.find {
            objective.getScore(it).score == score
        }?.let {
            scoreboard.resetScores(it)
        }
    }

    fun clear() {
        scoreboard.entries.forEach {
            scoreboard.resetScores(it)
        }
    }

    /* Delete every objective of the scoreboard */
    fun delete() {
        scoreboard.objectives.forEach { it.unregister() }
    }

    /* Assign the scoreboard to a specific player */
    fun assignTo(player:Player) {
        player.scoreboard = scoreboard
    }

    /**
    * @param updateInterval:Long the refresh rate of the scoreboard (In Ticks)
    * @param lineUpdates:Pair<Int, () -> Component> The actual lines to update
     */
    fun updateLines(updateInterval:Long, vararg lineUpdates:Pair<Int, () -> Component>) {
        lineUpdates.forEach { (score, _) ->
            remove(score)
        }

        updatableLines.clear()

        lineUpdates.forEach { (score, updateFunc) ->
            updatableLines[score] = LineUpdate(
                get(score) ?: Component.empty(),
                updateFunc
            )
        }

        object : BukkitRunnable() {
            override fun run() {
                updatableLines.forEach { (score, lineUpdate) ->
                    val newComponent = lineUpdate.updateFunction()
                    set(newComponent, score)
                }
            }
        }.runTaskTimer(plugin, 0L, updateInterval)
    }
}