package gg.flyte.twilight.event

import gg.flyte.twilight.Twilight
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

/**
 * Creates a Bukkit event listener and registers it to listen for a specific type of event.
 *
 * This function simplifies the process of creating and registering a Bukkit event listener.
 *
 * @param T The type of event to listen for, specified as a reified type parameter.
 * @param priority The priority at which the listener should be called, default is EventPriority.NORMAL.
 * @param callback A lambda function to be executed when the specified event occurs.
 * @return A `TwilightListener` instance, which can be used to later unregister the listener.
 */
inline fun <reified T : Event> event(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline callback: T.() -> Unit,
): TwilightListener =
    TwilightListener().apply {
        Twilight.plugin.server.pluginManager.registerEvent(
            T::class.java, // tell bukkit what event we're listening to
            this, // the listener but we don't need it
            priority, // the event priority, can be changed in the code but by default its 'normal'
            { _, event -> // the 'opening' of the event executor
                if (event is T) callback(event) // event executor body
            },
            Twilight.plugin, // pass the instance to bukkit, so it knows what plugin should listen to the event
            true
        )
    }

/**
 * Layer on top of Bukkit's Listener to allow for easy unregistering. Returned by the above function.
 */
class TwilightListener : Listener {
    fun unregister() = HandlerList.unregisterAll(this)
}