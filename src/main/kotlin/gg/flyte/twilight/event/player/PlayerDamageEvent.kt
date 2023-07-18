package gg.flyte.twilight.event.player

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class PlayerDamageEvent(damaged: Player, cause: DamageCause, damage: Double) : EntityDamageEvent(damaged, cause, damage)