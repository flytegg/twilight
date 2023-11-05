package gg.flyte.twilight.event.custom.interact

import gg.flyte.twilight.event.TwilightEvent
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

/**
 * Represents an event where a player interacts with an item in their main-hand.
 *
 * @param player The Player who triggered the event.
 * @param action The action performed by the player (e.g., LEFT_CLICK_AIR, RIGHT_CLICK_BLOCK).
 * @param item The ItemStack in the player's off-hand.
 * @param clickedBlock The Block that was clicked, if applicable.
 * @param clickedFace The face of the clicked block, if applicable.
 * @param clickedPosition The position where the player clicked, if applicable.
 */
class PlayerMainHandInteractEvent(
    val player: Player,
    val action: Action,
    val item: ItemStack?,
    val clickedBlock: Block?,
    val clickedFace: BlockFace,
    val clickedPosition: Vector?
) : TwilightEvent()