package gg.flyte.twilight.event.entity

import gg.flyte.twilight.event.Event
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

open class EntityEvent(val entity: Entity) : Event()
