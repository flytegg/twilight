package gg.flyte.twilight.scoreboards

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.UUID

/**
 * Create a static sidebar for multiple players
 */
fun createStaticSidebar(players: MutableList<UUID>, title: String, lines: MutableMap<String, Int>) {
    val board = Bukkit.getScoreboardManager().newScoreboard

    val obj = board.registerNewObjective("title", "dummy")
    obj.displayName(Component.text(title))
    obj.displaySlot = DisplaySlot.SIDEBAR

    lines.forEach { (text, score) ->
        val team = board.registerNewTeam("line_${score}")
        val entry = "§${score}"

        team.prefix(Component.text(text))
        team.addEntry(entry)

        obj.getScore(entry).score = score
    }

    players.forEach { uuid ->
        val player = Bukkit.getPlayer(uuid)
        player?.scoreboard = board
        player?.let {
            ScoreboardSaver.add(uuid, board)
        }
    }
}

/**
 * Remove scoreboard from a player
 */
fun removeScoreboard(uuid: UUID) {
    val player = Bukkit.getPlayer(uuid)
    player?.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    ScoreboardSaver.remove(uuid)
}

/**
 * Clear all active scoreboards
 */
fun clearBoards() {
    ScoreboardSaver.clear()
}