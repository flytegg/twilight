package gg.flyte.twilight.boards

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import java.util.UUID

object TwilightScoreboard {

    /* Player-specific sidebar scoreboards */
    private val sidebarBoards = mutableMapOf<UUID, Scoreboard>()

    /* Player-specific tablist scoreboards */
    private val tablistBoards = mutableMapOf<UUID, Scoreboard>()

    /* Sidebar methods */
    fun addSideboardPlayer(uuid: UUID, scoreboard: Scoreboard) {
        sidebarBoards[uuid] = scoreboard
    }

    fun removeSideboardPlayer(uuid: UUID) {
        sidebarBoards.remove(uuid)
    }

    fun clearSideboards() {
        sidebarBoards.clear()
    }

    /* Tablist methods */
    fun addTablistPlayer(uuid: UUID, scoreboard: Scoreboard) {
        tablistBoards[uuid] = scoreboard
    }

    fun removeTablistPlayer(uuid: UUID) {
        tablistBoards.remove(uuid)
    }

    fun clearTablists() {
        tablistBoards.clear()
    }
}