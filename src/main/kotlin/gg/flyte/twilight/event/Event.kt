package gg.flyte.twilight.event

import gg.flyte.twilight.Twilight
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

inline fun <reified T : Event> listen(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline callback: (T) -> Unit,
) {
    Twilight.plugin.server.pluginManager.registerEvent(
        T::class.java, // tell bukkit what event we're listening to
        object: Listener {}, // the listener but we don't need it
        priority, // the event priority, can be changed in the code but by default its 'normal'
        { _, event -> // the 'opening' of the event executor
            if (event is T) callback(event) // event executor body
        },
        Twilight.plugin, // pass the instance to bukkit, so it knows what plugin should listen to the event
        true
    )
}