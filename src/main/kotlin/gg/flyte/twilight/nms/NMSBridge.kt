package gg.flyte.twilight.nms

import org.bukkit.entity.Entity

interface NMSBridge {

    fun onGround(bukkitEntity: Entity): Boolean

}