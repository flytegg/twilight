package gg.flyte.twilight.events.custom

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * Custom event class representing a player interaction event involving the off hand.
 *
 * @param player The player who triggered the interaction.
 * @param action The type of interaction action (RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR, etc.).
 * @param item The ItemStack in the player's off hand during the interaction.
 * @param clickedBlock The block the player interacted with, if applicable (null for air interactions).
 * @param clickedFace The face of the clicked block that was interacted with.
 */
class PlayerOffHandInteractEvent(
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
)