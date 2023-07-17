package gg.flyte.twilight.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.time.Instant

open class Event : Event() {
    val timestamp: Instant = Instant.now()

    companion object {
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}