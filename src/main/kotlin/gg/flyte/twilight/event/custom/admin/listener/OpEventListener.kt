package gg.flyte.twilight.event.custom.admin.listener

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.CustomTwilightListener
import gg.flyte.twilight.event.custom.admin.PlayerDeopEvent
import gg.flyte.twilight.event.custom.admin.PlayerOpChangeEvent
import gg.flyte.twilight.event.custom.admin.PlayerOpEvent
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.gson.GSON
import gg.flyte.twilight.scheduler.repeat
import java.io.File
import java.util.*

object OpEventListener : CustomTwilightListener() {
    private val opsFile = File("${Twilight.plugin.dataFolder}/../../ops.json")
    private var opsData = mutableSetOf<OpData>()
    private var opsLastChanged: Long? = null

    init {
        if (opsFile.exists()) {
            opsLastChanged = opsFile.lastModified()
            opsData += getOpsData()
        }

        runnables += repeat(async = true) {
            if (!opsFile.exists()) return@repeat cancel()
            if (!hasOpsChanged()) return@repeat

            val newOpsData = getOpsData()

            // Removed ops
            opsData.subtract(newOpsData).toSet().applyForEach {
                opsData -= this
                Twilight.plugin.server.pluginManager.callEvent(PlayerDeopEvent(uuid, name))
                Twilight.plugin.server.pluginManager.callEvent(PlayerOpChangeEvent(uuid, name))
            }

            // Added ops
            newOpsData.subtract(opsData).toSet().applyForEach {
                opsData += this
                Twilight.plugin.server.pluginManager.callEvent(PlayerOpEvent(uuid, name))
                Twilight.plugin.server.pluginManager.callEvent(PlayerOpChangeEvent(uuid, name))
            }
        }
    }

    data class OpData(
        val uuid: UUID,
        val name: String,
        val level: Int,
        val bypassPlayerLimit: Boolean
    )

    private fun getOpsData(): Set<OpData> = GSON.fromJson(opsFile.reader(), Array<OpData>::class.java).toSet()

    private fun hasOpsChanged(): Boolean = (opsLastChanged ?: 0) < opsFile.lastModified()

}
