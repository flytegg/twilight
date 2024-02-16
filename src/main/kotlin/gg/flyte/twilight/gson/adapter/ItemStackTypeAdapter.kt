package gg.flyte.twilight.gson.adapter

import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gg.flyte.twilight.gson.TwilightTypeAdapter
import org.bukkit.inventory.ItemStack

class ItemStackTypeAdapter(override var allowNull: Boolean = false) : TwilightTypeAdapter<ItemStack>(allowNull) {

    override fun writeSafe(writer: JsonWriter, instance: ItemStack) {
        writer.jsonValue(defaultGson.toJson(instance.serialize()))
    }

    @Suppress("unchecked_cast")
    override fun read(reader: JsonReader): ItemStack {
        val json = defaultGson.fromJson(JsonParser.parseReader(reader).asJsonObject, Map::class.java) as Map<String, Any>
        println("json = $json")
        return ItemStack.deserialize(json)
    }

}