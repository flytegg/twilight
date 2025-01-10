package gg.flyte.twilight.gson.adapter

import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gg.flyte.twilight.gson.TwilightTypeAdapter
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization

class ConfigurationSerializationTypeAdapter<T : ConfigurationSerializable>(override var allowNull: Boolean = false) : TwilightTypeAdapter<T>(allowNull) {

    override fun writeSafe(writer: JsonWriter, instance: T) {
        writer.jsonValue(defaultGson.toJson(instance.serialize()))
    }

    @Suppress("unchecked_cast")
    override fun read(reader: JsonReader): T {
        val json = defaultGson.fromJson(JsonParser.parseReader(reader).asJsonObject, Map::class.java) as Map<String, Any>
        return ConfigurationSerialization.deserializeObject(json) as? T ?: throw IllegalStateException("Failed to deserialize object")
    }

}