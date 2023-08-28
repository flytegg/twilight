package gg.flyte.twilight.events.custom

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * Custom event class representing a player interaction event involving the main hand.
 *
 * @param player The player who triggered the interaction.
 * @param action The type of interaction action (RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR, etc.).
 * @param item The ItemStack in the player's main hand during the interaction.
 * @param clickedBlock The block the player interacted with, if applicable (null for air interactions).
 * @param clickedFace The face of the clicked block that was interacted with.
 */
class PlayerMainHandInteractEvent(
    player: Player,
    action: Action,
    item: ItemStack?,
    clickedBlock: Block?,
    clickedFace: BlockFace
) : PlayerInteractEvent(
    player,
    action,
    item,
    clickedBlock,
    clickedFace
), Cancellable {

    private var cancelled = false

    /**
     * Checks if the event is cancelled.
     *
     * @return True if the event is cancelled, false otherwise.
     */
    override fun isCancelled(): Boolean {
        return cancelled
    }

    /**
     * Sets the cancelled status of the event.
     *
     * @param cancel True to cancel the event, false to allow it.
     */
    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    companion object {
        val HANDLERS = HandlerList()

        /**
         * Returns the list of event handlers for this event.
         *
         * @return The HandlerList containing the event handlers.
         */
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    /**
     * Returns the list of event handlers for this event.
     *
     * @return The HandlerList containing the event handlers.
     */
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

}