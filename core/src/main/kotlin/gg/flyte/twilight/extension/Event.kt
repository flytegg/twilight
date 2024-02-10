package gg.flyte.twilight.extension

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Gets the Player associated with the InventoryClickEvent.
 *
 * This method retrieves the Player who triggered the InventoryClickEvent. Since the
 * InventoryClickEvent is always triggered by a HumanEntity, which is guaranteed to be a
 * Player, this convenience method returns the HumanEntity as a Player directly. It safely
 * performs a cast of the whoClicked property to Player.
 *
 * @return The Player who triggered the InventoryClickEvent.
 */
fun InventoryClickEvent.getPlayer(): Player {
    return whoClicked as Player
}