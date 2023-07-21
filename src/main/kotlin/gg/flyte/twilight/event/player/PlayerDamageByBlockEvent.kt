package gg.flyte.twilight.event.player

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByBlockEvent

class PlayerDamageByBlockEvent(damager: Block?, damaged: Player, cause: DamageCause, damage: Double) :
    EntityDamageByBlockEvent(damager, damaged, cause, damage)