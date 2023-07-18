package gg.flyte.twilight.event

import gg.flyte.twilight.event.player.listener.PlayerDamageListener
import gg.flyte.twilight.event.player.listener.PlayerMoveBlockListener
import org.bukkit.event.Listener

object CustomEventRegistry {
    private val eventListeners = setOf(
        PlayerMoveBlockListener, PlayerDamageListener
    )

    private var activeEventListeners = eventListeners.toMutableSet()

    private var hasBeenCalled = false

    fun none() {
        if (hasBeenCalled) throw IllegalCallerException("Only one of the event methods can be called. none(), enable(), or disable()")
        hasBeenCalled = true
        activeEventListeners.clear()
    }

    fun enable(vararg eventListeners: Listener) {
        if (hasBeenCalled) throw IllegalCallerException("Only one of the event methods can be called. none(), enable(), or disable()")
        hasBeenCalled = true
        activeEventListeners.clear()
        activeEventListeners.addAll(eventListeners)
    }

    fun disable(vararg eventListeners: Listener) {
        if (hasBeenCalled) throw IllegalCallerException("Only one of the event methods can be called. none(), enable(), or disable()")
        hasBeenCalled = true
        eventListeners.forEach {
            activeEventListeners.remove(it)
        }
    }
}