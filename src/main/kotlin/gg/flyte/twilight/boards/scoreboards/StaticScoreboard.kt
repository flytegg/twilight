package gg.flyte.twilight.boards.scoreboards

import gg.flyte.twilight.boards.TwilightScoreboard.addSideboardPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID


class StaticScoreboard(val players: List<UUID>, val title: Component, val lines: List<Component>) {
    /*
    @param players:List<UUID> players that will be able to see this scoreboard
    @param title:String Title of the actual scoreboard, you can use ChatColors.
    @param lines:List<Component> all the actual content of the scoreboard, Max raws for scoreboard is 16.
    */
    fun createStaticSidebar() {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val objective = board.registerNewObjective("title", "dummy").apply {
            displayName(title)
            displaySlot = DisplaySlot.SIDEBAR
        }

        lines.forEachIndexed { index, line ->
            val score = lines.size - index
            val team = board.registerNewTeam("line_$score")
            val entry = "§$score"

            team.prefix(line)
            team.addEntry(entry)
            objective.getScore(entry).score = score
        }

        players.forEach { uuid ->
            Bukkit.getPlayer(uuid)?.let { player ->
                player.scoreboard = board
                addSideboardPlayer(uuid, board)
            }
        }
    }

}