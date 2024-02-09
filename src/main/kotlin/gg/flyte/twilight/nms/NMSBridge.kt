package gg.flyte.twilight.nms

import org.bukkit.entity.LivingEntity

interface NMSBridge {
    fun isOnGround(entity: LivingEntity): Boolean
}