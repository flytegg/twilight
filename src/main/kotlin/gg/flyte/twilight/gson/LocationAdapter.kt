package gg.flyte.twilight.gson

import com.google.gson.*
import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.reflect.Type

object LocationAdapter : JsonDeserializer<Location>, JsonSerializer<Location> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): Location {
        if (json == null) throw JsonParseException("JSON cannot be null.")
        if (json !is JsonObject) throw JsonParseException("Not a valid JSON Object.")

        val world = json.get("world")
        val x = json.get("x")
        val y = json.get("y")
        val z = json.get("z")
        val yaw = json.get("yaw")
        val pitch = json.get("pitch")

        if (world == null || x == null || y == null || z == null)
            throw JsonParseException("Invalid JSON format, some required values are null.")

        if (!world.isJsonPrimitive && !(world as JsonPrimitive).isString) throw JsonParseException("\"world\" not of type string.")
        if (!x.isJsonPrimitive && !(x as JsonPrimitive).isNumber) throw JsonParseException("\"x\" not of type number.")
        if (!y.isJsonPrimitive && !(y as JsonPrimitive).isNumber) throw JsonParseException("\"y\" not of type number.")
        if (!z.isJsonPrimitive && !(z as JsonPrimitive).isNumber) throw JsonParseException("\"z\" not of type number.")

        if (yaw != null && !yaw.isJsonPrimitive && !(yaw as JsonPrimitive).isNumber) throw JsonParseException("\"yaw\" not of type number.")
        if (pitch != null && !pitch.isJsonPrimitive && !(pitch as JsonPrimitive).isNumber) throw JsonParseException("\"pitch\" not of type number.")

        val worldInstance = Bukkit.getWorld(world.asString) ?: throw IllegalArgumentException("Invalid world \"$world\".")
        return Location(worldInstance, x.asDouble, y.asDouble, z.asDouble, yaw?.asFloat ?: 0F, pitch?.asFloat ?: 0F)
    }

    override fun serialize(location: Location?, type: Type?, context: JsonSerializationContext?): JsonElement {
        if (location == null) throw JsonParseException("Location cannot be null.")
        return JsonObject().apply {
            addProperty("world", location.world?.name)
            addProperty("x", location.x)
            addProperty("y", location.y)
            addProperty("z", location.z)
            addProperty("yaw", location.yaw)
            addProperty("pitch", location.pitch)
        }
    }
}