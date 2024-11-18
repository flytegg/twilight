package gg.flyte.twilight.scoreboards

import org.bukkit.scoreboard.Scoreboard
import java.util.UUID

object ScoreboardSaver {
    private val boards = mutableMapOf<UUID, Scoreboard>()

    fun remove(id: UUID) { boards.remove(id) }
    fun clear() { boards.clear() }
    fun add(uuid:UUID, scoreboard: Scoreboard) { boards[uuid] = scoreboard }
}