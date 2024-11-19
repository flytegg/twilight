package gg.flyte.twilight.boards.scoreboards

import gg.flyte.twilight.boards.TwilightScoreboard.addSideboardPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DynamicScoreboard(
    private val player: UUID,
    private val title: Component,
    private val linesList: List<List<Component>>,
    private val delay: Long,
    private val plugin: JavaPlugin
) {
    val board = Bukkit.getScoreboardManager().newScoreboard
    private var currentListIndex = 0

    fun createDynamicSidebar() {
        val objective = board.registerNewObjective("title", "dummy").apply {
            displayName(title)
            displaySlot = DisplaySlot.SIDEBAR
        }

        object : BukkitRunnable() {
            override fun run() {
                val currentLines = linesList[currentListIndex]
                currentLines.forEachIndexed { index, line ->
                    val score = currentLines.size - index
                    val team = board.registerNewTeam("line_$score")
                    val entry = "§$score"

                    team.prefix(line)
                    team.addEntry(entry)
                    objective.getScore(entry).score = score
                }

                /* Handles the Scoreboard updating System */

                currentListIndex = (currentListIndex + 1) % linesList.size
            }
        }.runTaskTimer(plugin, 0L, delay)
    }

    fun assignPlayer() {
        val currentPlayer = Bukkit.getPlayer(player) ?: return
        currentPlayer.scoreboard = board
        addSideboardPlayer(currentPlayer.uniqueId, board)
    }
}