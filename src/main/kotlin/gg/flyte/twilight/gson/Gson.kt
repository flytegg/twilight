package gg.flyte.twilight.gson

import com.google.gson.GsonBuilder

val GSON = GsonBuilder().setPrettyPrinting().create()!!

/**
 * Converts an object to its JSON representation using Google's Gson library.
 *
 * @receiver The object to be converted to JSON.
 * @return The JSON representation of the object.
 */
fun Any.toJson() = GSON.toJson(this)