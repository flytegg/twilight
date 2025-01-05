package gg.flyte.twilight.scoreboard

import gg.flyte.twilight.string.toMini
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard


/**
 * Manages below-name scoreboard display
 * @param plugin The JavaPlugin instance
 */
class TwilightBelowName(private val plugin: JavaPlugin) {
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective = scoreboard.registerNewObjective("twilight-below-name", "dummy").apply {
        displaySlot = DisplaySlot.BELOW_NAME
    }

    /**
     * Sets the display name for the below-name objective
     * @param text Display name (supports MiniMessage format)
     */
    fun displayName(text: String) = objective.displayName(text.toMini())

    /**
     * Sets a score for a player
     * @param player Target player
     * @param score Numeric score to display
     */
    fun set(player: Player, score: Int) {
        objective.getScore(player.name).score = score
        updateScoreboardForAll()
    }

    /**
     * Gets the current score of a player
     * @param player Target player
     * @return Current score
     */
    fun get(player: Player): Int = objective.getScore(player.name).score

    /**
     * Removes all objectives and cleans up
     */
    fun delete() {
        scoreboard.objectives.forEach { it.unregister() }
        Bukkit.getOnlinePlayers().forEach {
            it.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }
    }

    /**
     * Assigns the scoreboard to a player
     * @param player Target player
     */
    fun assignTo(player: Player) {
        player.scoreboard = scoreboard
        updateScoreboardForAll()
    }

    /**
     * Removes the scoreboard from a player
     * @param player Target player
     */
    fun removeFrom(player: Player) { player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard }

    private fun updateScoreboardForAll() {
        Bukkit.getOnlinePlayers().forEach { it.scoreboard = scoreboard }
    }
}