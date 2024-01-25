package gg.flyte.twilight.extension

import gg.flyte.twilight.gson.toJson
import org.bson.Document

/**
 * Converts a [T] object into a [Document].
 * @return The [Document] representation of the object.
 */
inline fun <reified T : Any> T.toDocument(): Document = Document.parse(this.toJson())