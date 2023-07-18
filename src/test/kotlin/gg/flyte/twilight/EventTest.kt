package gg.flyte.twilight

import gg.flyte.twilight.event.entity.EntityEvent
import gg.flyte.twilight.event.player.PlayerEvent
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun main() {
    entityEventTest()
    playerEventTest()
}

lateinit var entity: Entity
lateinit var player: Player

fun entityEventTest() {
    val event = EntityEvent(entity)
    val eventEntity = event.entity
}

fun playerEventTest() {
    val event = PlayerEvent(player)
    val eventPlayer = event.player
}
