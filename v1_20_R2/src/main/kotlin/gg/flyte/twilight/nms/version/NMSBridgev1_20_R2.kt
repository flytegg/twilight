package gg.flyte.twilight.nms.version

import gg.flyte.twilight.nms.NMSBridge
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity
import org.bukkit.entity.Entity

class NMSBridgev1_20_R2 : NMSBridge {

    override fun onGround(bukkitEntity: Entity): Boolean = with((bukkitEntity as CraftEntity).handle) {
        val previous = aJ // capture the previous onGround state
        c(true) // force the onGround state to be true to trigger updating mainSupportingBlockPos
        return aD.isPresent.also { c(previous) } // return onGround and restore the previous onGround state
    }

}