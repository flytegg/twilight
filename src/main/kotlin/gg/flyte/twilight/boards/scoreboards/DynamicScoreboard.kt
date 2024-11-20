package gg.flyte.twilight.boards.scoreboards

import gg.flyte.twilight.boards.TwilightScoreboard.addSideboardPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/* 
* @param players:List<UUID> list Player's UUID for assigning the sidebar
* @param title:Component Scoreboard title
* @param linesList:List<List<Component>> List of line patterns for animation
* @param delay:Long Interval between pattern changes
* @param plugin:JavaPlugin Instance of a class extending JavaPlugin for the Runnable.
*/
class DynamicScoreboard(
    private val title:Component,
    private val linesList:List<List<Component>>,
    private val delay:Long,
    private val plugin:JavaPlugin
) {
    /* Server scoreboard */
    val board = Bukkit.getScoreboardManager().newScoreboard

    /* Current index for cycling through patterns */
    private var currentListIndex = 0
    
    fun createDynamicSidebar() {
        val objective = board.registerNewObjective("title", "dummy").apply {
            displayName(title)
            displaySlot = DisplaySlot.SIDEBAR
        }

        /* Create teams for each possible line */
        val maxLines = linesList.maxOfOrNull { it.size } ?: 0
        repeat(maxLines) { index ->
            val score = maxLines - index
            board.registerNewTeam("line_$score")
        }

        /* update scoreboard dynamically */
        object : BukkitRunnable() {
            override fun run() {
                val currentLines = linesList[currentListIndex]
                currentLines.forEachIndexed { index, line ->
                    val score = currentLines.size - index
                    val team = board.getTeam("line_$score")
                    val entry = "§$score"

                    team?.prefix(line)
                    team?.addEntry(entry)
                    objective.getScore(entry).score = score
                }

                /* Cycle to next pattern */
                currentListIndex = (currentListIndex + 1) % linesList.size
            }
        }.runTaskTimer(plugin, 0L, delay)
    }

    /* Assign scoreboard to player */
    fun assignPlayer(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid)
        player?.scoreboard = board
        addSideboardPlayer(uuid, board)
    }
}