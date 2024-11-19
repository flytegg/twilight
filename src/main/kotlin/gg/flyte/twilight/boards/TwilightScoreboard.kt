package gg.flyte.twilight.boards

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import java.util.UUID

object TwilightScoreboard {

    /* Player-specific sidebar scoreboards */
    private val sidebarBoards = mutableMapOf<UUID, Scoreboard>()

    /* Sidebar methods */
    fun addSideboardPlayer(uuid: UUID, scoreboard: Scoreboard) {
        sidebarBoards[uuid] = scoreboard
    }

    fun removeSideboardPlayer(uuid: UUID) {
        sidebarBoards.remove(uuid)
    }

    fun clearSideboards() {
        sidebarBoards.clear()
        for (uuid in sidebarBoards.keys) {
            val player = Bukkit.getPlayer(uuid)
            player?.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }
    }

}