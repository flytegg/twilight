package gg.flyte.twilight.boards.scoreboards

import gg.flyte.twilight.boards.TwilightScoreboard.addSideboardPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

/*
@param players:List<UUID> player that will be able to see this scoreboard
@param title:String Title of the actual scoreboard, you can use ChatColors.
@param lines:List<Component> all the actual content of the scoreboard, Max raws for scoreboard is 16.
*/
class StaticScoreboard(private val players:List<UUID>, private val title: Component, private val lines: List<Component>) {
    val board = Bukkit.getScoreboardManager().newScoreboard
    fun createStaticSidebar() {
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
    }

    fun assignPlayer() {
        players.forEach { playeruuid ->
            val player = Bukkit.getPlayer(playeruuid)
            player?.scoreboard = board
            addSideboardPlayer(playeruuid, board)
        }
    }
}