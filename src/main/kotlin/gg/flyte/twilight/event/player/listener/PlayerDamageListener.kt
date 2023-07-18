package gg.flyte.twilight.event.player.listener

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.player.PlayerDamageByBlockEvent
import gg.flyte.twilight.event.player.PlayerDamageByEntityEvent
import gg.flyte.twilight.event.player.PlayerDamageByPlayerEvent
import gg.flyte.twilight.event.player.PlayerDamageEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object PlayerDamageListener : Listener {

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player) {
            Twilight.plugin.server.pluginManager.callEvent(
                PlayerDamageEvent(
                    entity,
                    event.cause,
                    event.damage
                )
            )
        }
    }

    @EventHandler
    fun onEntityDamageByBlock(event: EntityDamageByBlockEvent) {
        val entity = event.entity
        if (entity is Player) {
            Twilight.plugin.server.pluginManager.callEvent(
                PlayerDamageByBlockEvent(
                    event.damager,
                    entity,
                    event.cause,
                    event.damage
                )
            )
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity is Player) {
            val damager = event.damager
            Twilight.plugin.server.pluginManager.callEvent(
                if (damager is Player)
                    PlayerDamageByPlayerEvent(
                        damager,
                        entity,
                        event.cause,
                        event.damage
                    )
                else
                    PlayerDamageByEntityEvent(
                        damager,
                        entity,
                        event.cause,
                        event.damage
                    )
            )
        }
    }

}