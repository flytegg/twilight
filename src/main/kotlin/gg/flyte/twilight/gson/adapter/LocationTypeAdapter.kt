package gg.flyte.twilight.gson.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gg.flyte.twilight.gson.TwilightTypeAdapter
import org.bukkit.Bukkit
import org.bukkit.Location

class LocationTypeAdapter(override var allowNull: Boolean = false) : TwilightTypeAdapter<Location>(allowNull) {

    override fun writeSafe(writer: JsonWriter, instance: Location) {
        writer.apply {
            beginObject()
            property("world", instance.world!!.name)
            property("x", instance.x)
            property("y", instance.y)
            property("z", instance.z)
            property("yaw", instance.yaw)
            property("pitch", instance.pitch)
            endObject()
        }
    }

    override fun read(reader: JsonReader): Location = with(reader) {
        val location = Location(null, 0.0, 0.0, 0.0)
        beginObject()
        while (hasNext()) {
            when (nextName()) {
                "world" -> location.world = Bukkit.getWorld(nextString())
                "x" -> location.x = nextDouble()
                "y" -> location.y = nextDouble()
                "z" -> location.z = nextDouble()
                "yaw" -> location.yaw = nextDouble().toFloat()
                "pitch" -> location.pitch = nextDouble().toFloat()
            }
        }
        endObject()
        location
    }

}