package gg.flyte.twilight.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gg.flyte.twilight.Twilight

object ExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(attributes: FieldAttributes) = attributes.getAnnotation(Exclude::class.java) != null
    override fun shouldSkipClass(clazz: Class<*>) = false
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Exclude

val GSON: Gson = Twilight.gsonBuilder
    .addSerializationExclusionStrategy(ExclusionStrategy)
    .addDeserializationExclusionStrategy(ExclusionStrategy)
    .create()

open class TwilightTypeAdapter<T>(open var allowNull: Boolean = false) : TypeAdapter<T>() {

    val defaultGson: Gson = GsonBuilder()
        .addSerializationExclusionStrategy(ExclusionStrategy)
        .addDeserializationExclusionStrategy(ExclusionStrategy)
        .setPrettyPrinting()
        .create()

    open fun writeSafe(writer: JsonWriter, instance: T): Unit =
        throw NotImplementedError("Not implemented TwilightTypeAdapter#writeSafe")

    override fun write(writer: JsonWriter, instance: T?) {
        if (instance == null) {
            if (allowNull) writer.nullValue()
            else throw IllegalArgumentException("Cannot be null.")
            return
        }

        writeSafe(writer, instance)
    }

    override fun read(reader: JsonReader): T = throw NotImplementedError("Not implemented TwilightTypeAdapter#read")

    fun JsonWriter.property(name: String, value: Any?): JsonWriter {
        name(name)
        return when (value) {
            null -> nullValue()
            is String -> value(value)
            is Number -> value(value)
            is Boolean -> value(value)
            else -> jsonValue(value.toJson())
        }
    }

}

/**
 * Converts an object to its JSON representation using Google's Gson library.
 *
 * @receiver The object to be converted to JSON.
 * @return The JSON representation of the object.
 */
fun Any.toJson(): String = GSON.toJson(this)

/**
 * Converts an object to its JSON representation using Google's Gson library.
 *
 * @receiver The object to be converted to JSON.
 * @return The JSON representation of the object.
 */
fun Any.toJsonTree(): JsonElement = GSON.toJsonTree(this)