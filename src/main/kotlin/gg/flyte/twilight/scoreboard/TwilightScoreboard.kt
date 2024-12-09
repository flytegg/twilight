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
    private var objective = scoreboard.registerNewObjective("sidebar", "dummy")

    private val updatableLines = mutableMapOf<Int, LineUpdate>()

    private data class LineUpdate(
        val initialLine: Component,
        val updateFunction: () -> Component
    )

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    /**
     * @param title:Component the displayname of the scoreboard
     * Used for setting the displayname of a scoreboard
     */
    fun setName(title:Component) {
        objective.displayName(title)
    }

    /**
     * @param text:Component the component you want to use at that line
     * @param score:Int the score you want to set
     * Used to change a specific line.
     */
    fun set(text:Component, score:Int) {
        scoreboard.getTeam(" ".repeat(score))?.unregister()
        val team = scoreboard.registerNewTeam(" ".repeat(score))
        team.prefix(text)
        team.addEntry(" ".repeat(score))

        objective.getScore(" ".repeat(score)).score = score
    }

    /**
     * @param lines:Component actual components that you want in your scoreboard
     * Used to add how many lines you want to the scoreboard
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

    /**
     * @param score:Int the number of the line you want to get.
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
     * @param score:Int the line you want to remove.
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
     * Clear the scoreboard, just leaves the displayname
     */
    fun clear() {
        scoreboard.entries.forEach {
            scoreboard.resetScores(it)
        }
    }

    /**
     * Remove every objective of the score
     */
    fun delete() {
        scoreboard.objectives.forEach { it.unregister() }
    }

    /**
     * @param player:Player the player you want to assign the scoreboard to
     * Assign the scoreboard to a player (Use a for Loop if you want to assign this to multiple players)
     */
    fun assignTo(player:Player) {
        player.scoreboard = scoreboard
    }

    /**
    * @param updateInterval:Long the refresh rate of the scoreboard (In Ticks)
    * @param lineUpdates:Pair<Int, () -> Component> The actual lines to update
     * Used to create dynamic scoreboard, if you want, feel free to make the whole scoreboard with this
     * instead of with the #setAll, works the same except here it updates!
     */
    fun updateLines(updateInterval:Long, vararg lineUpdates:Pair<Int, () -> Component>) {
        updatableLines.clear()

        lineUpdates.forEach { (score, updateFunc) ->
            val uniqueEntry = "§${score}"

            scoreboard.getTeam(uniqueEntry)?.unregister()

            val team = scoreboard.registerNewTeam(uniqueEntry)
            team.addEntry(uniqueEntry)

            updatableLines[score] = LineUpdate(
                initialLine = Component.empty(),
                updateFunction = updateFunc
            )
        }

        object : BukkitRunnable() {
            override fun run() {
                try {
                    if (!scoreboard.objectives.contains(objective)) {
                        cancel()
                        return
                    }

                    updatableLines.forEach { (score, lineUpdate) ->
                        val newComponent = lineUpdate.updateFunction()
                        val uniqueEntry = "§${score}"

                        if (!scoreboard.entries.contains(uniqueEntry)) {
                            return@forEach
                        }

                        var team = scoreboard.getTeam(uniqueEntry)
                        if (team == null) {
                            team = scoreboard.registerNewTeam(uniqueEntry)
                            team.addEntry(uniqueEntry)
                        }

                        team.prefix(newComponent)

                        try {
                            objective.getScore(uniqueEntry).score = score
                        } catch (e: IllegalStateException) {

                        }
                    }
                } catch (e: Exception) {
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, updateInterval)
    }
}