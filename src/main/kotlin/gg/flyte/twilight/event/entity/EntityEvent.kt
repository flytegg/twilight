package gg.flyte.twilight.event.entity

import gg.flyte.twilight.event.Event
import org.bukkit.entity.Entity

open class EntityEvent(val entity: Entity) : Event()
