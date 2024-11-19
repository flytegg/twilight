package gg.flyte.twilight.scoreboards

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

object Scoreboard {
    /*
    @param players:List<UUID> players that will be able to see this scoreboard
    @param title:String Title of the actual scoreboard, you can use ChatColors.
    @param lines:List<String> all the actual content of the scoreboard, Max strings for scoreboard is 16.
     */
    fun createStaticSidebar(players: List<UUID>, title: String, lines: List<String>) {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val objective = board.registerNewObjective("title", "dummy").apply {
            displayName(Component.text(title))
            displaySlot = DisplaySlot.SIDEBAR
        }

        lines.forEachIndexed { index, line ->
            val score = lines.size - index
            val team = board.registerNewTeam("line_$score")
            val entry = "§$score"

            team.prefix(Component.text(line))
            team.addEntry(entry)
            objective.getScore(entry).score = score
        }

        players.forEach { uuid ->
            Bukkit.getPlayer(uuid)?.let { player ->
                player.scoreboard = board
                ScoreboardSaver.add(uuid, board)
            }
        }
    }

    fun removeScoreboard(players: List<UUID>) {
        players.forEach { uuid ->
            Bukkit.getPlayer(uuid)?.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
            ScoreboardSaver.remove(uuid)
        }
    }

    fun clearBoards() {
        ScoreboardSaver.clear()
    }
}