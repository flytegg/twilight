package gg.flyte.twilight.event.custom.admin

import gg.flyte.twilight.event.TwilightEvent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

/**
 * Represents an event that occurs when a player is de-op'd (de-promoted from operator status).
 * This event contains information about the affected player's UUID and name.
 */
class PlayerDeopEvent(
    val uuid: UUID,  // The UUID of the player being de-op'd.
    val name: String  // The name of the player being de-op'd.
) : TwilightEvent() {
    /**
     * Represents the offline player associated with this event.
     */
    val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)

    /**
     * Represents the online player associated with this event, if available.
     * This property will be null if the player is not currently online.
     */
    val player: Player? = offlinePlayer.player
}