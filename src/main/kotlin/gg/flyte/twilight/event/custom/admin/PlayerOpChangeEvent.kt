package gg.flyte.twilight.event.custom.admin

import gg.flyte.twilight.event.TwilightEvent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

/**
 * Represents an event that occurs when a player's operator status changes.
 * This event contains information about the affected player's UUID and name.
 */
class PlayerOpChangeEvent(
    val uuid: UUID,  // The UUID of the player whose operator status changed.
    val name: String  // The name of the player whose operator status changed.
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
