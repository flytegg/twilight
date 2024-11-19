package gg.flyte.twilight.boards

import net.kyori.adventure.text.Component
import org.bukkit.scoreboard.Scoreboard

import java.util.UUID

abstract class TwilightScoreboard {
    protected val boards = mutableMapOf<UUID, Scoreboard>()

    abstract fun createStaticSidebar(players: List<UUID>, title: Component, lines: List<Component>)
    abstract fun removeScoreboard(player: UUID)
    abstract fun clearBoards()

    fun add(uuid: UUID, scoreboard: Scoreboard) {
        boards[uuid] = scoreboard
    }

    fun remove(id: UUID) { boards.remove(id) }
    fun clear() { boards.clear() }
    fun getUUIDs(): List<UUID> = boards.keys.toList()
}