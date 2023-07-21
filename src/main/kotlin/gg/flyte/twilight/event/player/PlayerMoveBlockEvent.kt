package gg.flyte.twilight.event.player

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveBlockEvent(player: Player, from: Location, to: Location) : PlayerMoveEvent(player, from, to)