
import gg.flyte.twilight.string.toMini
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

/**
 * Manages player display features including prefixes, suffixes, and header/footer.
 * @param plugin The JavaPlugin instance
 */
class TwilightDisplay(private val plugin: JavaPlugin) {
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private var updateTask: BukkitTask? = null

    /**
     * Sets a prefix for a player, visible to all players
     * @param player Target player
     * @param prefix Prefix text (supports MiniMessage format)
     */
    fun prefix(player: Player, prefix: String) {
        team(player).prefix(prefix.toMini())
        updateScoreboardForAll()
    }

    /**
     * Sets a suffix for a player, visible to all players
     * @param player Target player
     * @param suffix Suffix text (supports MiniMessage format)
     */
    fun suffix(player: Player, suffix: String) {
        team(player).suffix(suffix.toMini())
        updateScoreboardForAll()
    }

    /**
     * Updates tab list header and footer with dynamic content
     * @param player Target player
     * @param updateInterval Update frequency in ticks
     * @param headerUpdater Function providing header content
     * @param footerUpdater Function providing footer content
     */
    fun updateHeaderFooter(
        player: Player,
        updateInterval: Long,
        headerUpdater: () -> String,
        footerUpdater: () -> String
    ) {
        updateTask?.cancel()
        updateTask = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            player.sendPlayerListHeaderAndFooter(
                headerUpdater().toMini(),
                footerUpdater().toMini()
            )
        }, 0L, updateInterval)
    }

    /**
     * Removes all display modifications from a player
     * @param player Target player
     */
    fun removeFrom(player: Player) {
        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty())
        player.playerListName(Component.text(player.name))
        team(player).removeEntry(player.name)
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }

    /**
     * Cleans up all display modifications and resets all players
     */
    fun delete() {
        updateTask?.cancel()
        updateTask = null
        scoreboard.teams.forEach { it.unregister() }
        Bukkit.getOnlinePlayers().forEach {
            it.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }
    }

    private fun team(player: Player): Team {
        return scoreboard.getTeam("twilight-${player.uniqueId}") ?: scoreboard.registerNewTeam("twilight-${player.uniqueId}").apply {
            addEntry(player.name)
        }
    }

    private fun updateScoreboardForAll() = Bukkit.getOnlinePlayers().forEach { it.scoreboard = scoreboard }
}