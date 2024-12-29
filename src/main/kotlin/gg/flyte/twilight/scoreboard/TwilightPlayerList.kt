package gg.flyte.twilight.scoreboard

import gg.flyte.twilight.string.toMini
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class TwilightPlayerList(private val plugin: JavaPlugin) {
    private var updateTask: BukkitTask? = null

    /**
     * @param player the player you want to set the header and footer for
     * @param headerUpdater Header content of the player list
     * @param footerUpdater Footer content of the player list
     * Used to set the header and footer of the player list
     */
    fun headerFooter(
        player: Player,
        headerUpdater: () -> String,
        footerUpdater: () -> String
    ) {
        player.sendPlayerListHeaderAndFooter(
            headerUpdater().toMini(),
            footerUpdater().toMini()
        )
    }

    /**
     * @param player the player you want to modify
     * @param displayName the new display name for the player
     * Used to set a player's display name in the player list
     */
    fun playerDisplayName(player: Player, displayName: String) {
        player.playerListName(displayName.toMini())
    }

    /**
     * @param updateInterval the refresh rate of the player list (In Ticks)
     * @param headerUpdater Header content of the player list
     * @param footerUpdater Footer content of the player list
     * Used to create dynamic player list
     */
    fun updateHeaderFooter(
        player: Player,
        updateInterval: Long,
        headerUpdater: () -> String,
        footerUpdater: () -> String
    ) {
        updateTask?.cancel()

        updateTask = object : BukkitRunnable() {
            override fun run() {
                player.sendPlayerListHeaderAndFooter(
                    headerUpdater().toMini(),
                    footerUpdater().toMini()
                )
            }
        }.runTaskTimer(plugin, 0L, updateInterval)
    }

    /**
     * @param player the player you want to remove the player list modifications from
     * Remove the player list modifications from a specific player
     */
    fun removeFrom(player: Player) {
        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty())
        player.playerListName(Component.text(player.name))
    }

    /**
     * Clean up resources and stop any ongoing updates
     */
    fun delete() {
        updateTask?.cancel()
        updateTask = null
    }
}