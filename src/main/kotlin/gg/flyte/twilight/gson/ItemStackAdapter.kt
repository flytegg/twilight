package gg.flyte.twilight.gson

import com.google.gson.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Type
import gg.flyte.twilight.extension.enumValue
import gg.flyte.twilight.builders.item.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment

object ItemStackAdapter: JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): ItemStack {
        if (json == null) throw JsonParseException("JSON cannot be null.")
        if (json !is JsonObject) throw JsonParseException("Not a valid JSON Object.")

        val materialType = json.get("type")
        val amount = json.get("amount").asInt
        val meta = json.getAsJsonObject("meta")

        if(materialType == null) throw JsonParseException("Invalid JSON format, some required values are null.")


        if (!materialType.isJsonPrimitive && !(materialType as JsonPrimitive).isString) throw JsonParseException("\"type\" not of type string.")
        val material = enumValue<Material>(materialType.asString) ?: throw JsonParseException("Invalid JSON, Invalid Material Provided.")
        val builder = ItemBuilder(material, amount)


        // Meta stuff
        if(meta != null) {
            val displayName = meta.get("displayName")
            val lore = meta.getAsJsonArray("lore")
            val enchants = meta.getAsJsonObject("enchants")
            val flags = meta.getAsJsonArray("flags")
            val unbreakable = meta.get("unbreakable")

            if(unbreakable != null && unbreakable.isJsonPrimitive && (unbreakable as JsonPrimitive).isBoolean) {
                builder.unbreakable = unbreakable.asBoolean
            }

            if(displayName != null && displayName.isJsonPrimitive && (displayName as JsonPrimitive).isString) {
                builder.name = Component.text(displayName.asString)
            }

            if(lore != null) {
                builder.lore = lore.map { Component.text(it.asString) }.toMutableList()
            }

            if(enchants != null && !enchants.isEmpty) {
                enchants.asMap().forEach{(enchant, level) ->
                    builder.enchantments[Enchantment.getByName(enchant)!!] = level.asInt
                }
            }

            if(flags != null) {
                // ...
            }
        }

        return builder.build()
    }

    override fun serialize(itemStack: ItemStack?,type: Type?, context: JsonSerializationContext?): JsonElement {
       if(itemStack == null) throw JsonParseException("ItemStack cannot be null")
        return JsonObject().apply {
            addProperty("type", itemStack.type.name)
            addProperty("amount", itemStack.amount)
            // Meta
            if(itemStack.hasItemMeta()) {
                add("meta", JsonObject().apply {
                    val meta = itemStack.itemMeta
                    addProperty("displayName", meta.displayName)
                    addProperty("unbreakable", meta.isUnbreakable)
                    add("lore", GSON.toJsonTree(meta.lore()) as JsonArray)
                    add("enchants", JsonObject().apply {
                        meta.enchants.forEach { enchant ->
                            addProperty(enchant.key.key.key, enchant.value)
                        }
                    })
                    add("flags", GSON.toJsonTree(meta.itemFlags) as JsonArray)
                    if(meta is SkullMeta) {
                        add("skullData", JsonObject().apply {
                            addProperty("owner", meta.owner)
                        })
                    }
                })

            }
        }
    }
}