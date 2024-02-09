package gg.flyte.twilight.nms

import gg.flyte.twilight.Twilight
import org.bukkit.entity.Entity

object NMS : NMSBridge {

    val version: String = Twilight.server.javaClass.packageName.apply { substring(lastIndexOf('.') + 1) }
    private var bridge: NMSBridge = runCatching {
        Class.forName("gg.flyte.twilight.nms.version.NMSBridge$version")
            .getDeclaredConstructor()
            .newInstance() as NMSBridge
    }.getOrNull() ?: throw UnsupportedOperationException("Unsupported server version: $version")

    override fun onGround(bukkitEntity: Entity): Boolean = bridge.onGround(bukkitEntity)

}