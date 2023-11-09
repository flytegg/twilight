package gg.flyte.twilight.event.custom.chat

import gg.flyte.twilight.event.TwilightEvent
import org.bukkit.entity.Player

/**
 * Represents an event that is triggered when a chat component with a custom click event is interacted with.
 *
 * This event carries information about the player who clicked the chat component and the data that was
 * sent along with the click event, typically used to determine what action should be taken as a result
 * of the click.
 *
 * @param player The player who clicked the chat component.
 * @param data The array of strings sent with the click event.
 */
class ChatClickEvent(
    val player: Player,
    val data: Array<String>
) : TwilightEvent()