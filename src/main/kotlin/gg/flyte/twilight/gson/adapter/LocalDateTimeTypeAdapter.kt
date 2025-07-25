package gg.flyte.twilight.gson.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gg.flyte.twilight.gson.TwilightTypeAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter(override var allowNull: Boolean = true) : TwilightTypeAdapter<LocalDateTime>(allowNull) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    override fun writeSafe(writer: JsonWriter, instance: LocalDateTime) {
        writer.value(formatter.format(instance))
    }

    override fun read(reader: JsonReader): LocalDateTime = LocalDateTime.parse(reader.nextString(), formatter)
}