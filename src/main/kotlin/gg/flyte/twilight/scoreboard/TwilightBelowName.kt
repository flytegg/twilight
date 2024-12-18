package gg.flyte.twilight.scoreboard

import gg.flyte.twilight.string.toMini
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

class TwilightBelowName(private val plugin: JavaPlugin) {
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective = scoreboard.registerNewObjective("twilight-below-name", "dummy").apply {
        displaySlot = DisplaySlot.BELOW_NAME
    }

    /**
     * @param text Display name of the scoreboard.
     */
    fun displayName(text: String) {
        objective.displayName(text.toMini())
    }

    /**
     * @param player The player to want to assign the score
     * @param score the score you want to set
     */
    fun set(player: Player, score: Int) {
        scoreboard.entries.forEach { entry ->
            if (scoreboard.getObjective(DisplaySlot.BELOW_NAME)?.getScore(entry)?.score == 0) {
                scoreboard.resetScores(entry)
            }
        }

        objective.getScore(player.name).score = score
    }

    /**
     * Return current score of a player
     * @param player The player you want to get the score of.
     * @return The score
     */
    fun get(player: Player): Int = objective.getScore(player.name).score

    /**
     * Reset the scoreboard and remove every objective
     */
    fun delete() {
        scoreboard.objectives.forEach { it.unregister() }
    }

    /**
     * @param player The player you want to assign the scoreboard to.
     */
    fun assignTo(player: Player) {
        player.scoreboard = scoreboard
    }

    /**
     * @param player The one you want to remove the scoreboard from
     */
    fun removeFrom(player: Player) {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }
}