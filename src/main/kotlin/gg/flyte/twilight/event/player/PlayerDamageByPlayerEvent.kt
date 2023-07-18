package gg.flyte.twilight.event.player

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PlayerDamageByPlayerEvent(damager: Player, damaged: Player, cause: DamageCause, damage: Double) :
    EntityDamageByEntityEvent(damager, damaged, cause, damage)