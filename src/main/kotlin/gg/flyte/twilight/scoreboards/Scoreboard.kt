package gg.flyte.twilight.scoreboards

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

object Scoreboard {
    fun createStaticSidebar(players: MutableList<UUID>, title: String, lines: MutableMap<String, Int>) {
        val board = Bukkit.getScoreboardManager().newScoreboard

        val obj = board.registerNewObjective("title", "dummy")
        obj.displayName(Component.text(title))
        obj.displaySlot = DisplaySlot.SIDEBAR

        /*
        We need to add a different amount of spaces
        for every word otherwise it won't support
        same words repeated.
         */
        lines.forEach { (text, score) ->
            val team = board.registerNewTeam("line_${score}")
            val entry = "§${score}"

            val spacedText = "$text${" ".repeat(score)}"

            team.prefix(Component.text(spacedText))
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

    fun removeScoreboard(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid)
        player?.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        ScoreboardSaver.remove(uuid)
    }

    fun clearBoards() {
        ScoreboardSaver.clear()
    }
}