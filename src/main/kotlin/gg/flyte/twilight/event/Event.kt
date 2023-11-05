package gg.flyte.twilight.event

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.custom.interact.listener.InteractEventListener
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.inventory.GUIListener
import org.bukkit.event.*
import java.time.Instant

/**
 * Creates a Bukkit event listener and registers it to listen for a specific type of event.
 *
 * This function simplifies the process of creating and registering a Bukkit event listener.
 *
 * @param T The type of event to listen for, specified as a reified type parameter.
 * @param priority The priority at which the listener should be called, default is EventPriority.NORMAL.
 * @param ignoreCancelled Whether to ignore if the event was cancellled or not, default is false.
 * @param callback A lambda function to be executed when the specified event occurs.
 * @return A `TwilightListener` instance, which can be used to later unregister the listener.
 */
inline fun <reified T : Event> event(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: T.() -> Unit,
): TwilightListener = TwilightListener().apply {
    Twilight.plugin.server.pluginManager.registerEvent(
        T::class.java,
        this,
        priority,
        { _, event ->
            if (event is T) callback(event)
        },
        Twilight.plugin,
        ignoreCancelled
    )
}

/**
 * Layer on top of Bukkit's Listener to allow for easy unregistering. Returned by the above function.
 */
class TwilightListener : Listener {
    fun unregister() = HandlerList.unregisterAll(this)
}

open class CustomTwilightListener {
    /**
     * The list of events registered to this custom Twilight Event
     */
    val events = mutableListOf<TwilightListener>()

    fun unregister() = events.applyForEach { unregister() }
}

/**
 * Base class for custom events in the Twilight plugin.
 *
 * @param async Specifies if the event should be handled asynchronously (default: false).
 */
open class TwilightEvent(async: Boolean = false) : Event(async), Cancellable {

    /**
     * The timestamp when the event occurred.
     */
    val timestamp: Instant = Instant.now()

    /**
     * Checks if the event is cancelled.
     *
     * @return True if the event is cancelled, otherwise false.
     */
    override fun isCancelled(): Boolean = isCancelled

    /**
     * Sets the cancellation status of the event.
     *
     * @param cancel True to cancel the event, false to allow it to proceed.
     */
    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    companion object {
        val HANDLERS = HandlerList()

        /**
         * Retrieves the HandlerList for this event.
         *
         * @return The HandlerList for this event.
         */
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    /**
     * Gets the HandlerList for this event.
     *
     * @return The HandlerList for this event.
     */
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}

/**
 * List of custom Twilight Event Listeners
 */
val customEvents = listOf<CustomTwilightListener>(
    GUIListener,
    InteractEventListener
)

