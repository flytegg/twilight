package gg.flyte.twilight.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder

object ExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(attributes: FieldAttributes) = attributes.getAnnotation(Exclude::class.java) != null

    override fun shouldSkipClass(clazz: Class<*>) =false
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Exclude

val GSON = GsonBuilder().setPrettyPrinting().addSerializationExclusionStrategy(ExclusionStrategy).create()!!

/**
 * Converts an object to its JSON representation using Google's Gson library.
 *
 * @receiver The object to be converted to JSON.
 * @return The JSON representation of the object.
 */
fun Any.toJson(): String = GSON.toJson(this)