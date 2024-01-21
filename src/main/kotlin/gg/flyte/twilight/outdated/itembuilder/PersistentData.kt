package gg.flyte.twilight.outdated.itembuilder

import org.bukkit.persistence.PersistentDataType
import java.util.*

data class PersistentData<Z>(
    val dataType: PersistentDataType<Z, Z>,
    val value: Z
)

//fun stringPdc(value: String) = PersistentData(PersistentDataType.STRING, value)
fun uuidPdc(value: UUID = UUID.randomUUID()) = value.toString()
//fun intPdc(value: Int) = PersistentData(PersistentDataType.INTEGER, value)
//fun doublePdc(value: Double) = PersistentData(PersistentDataType.DOUBLE, value)
//fun floatPdc(value: Float) = PersistentData(PersistentDataType.FLOAT, value)
//fun longPdc(value: Long) = PersistentData(PersistentDataType.LONG, value)
//fun booleanPdc(value: Boolean) = PersistentData(PersistentDataType.BYTE, if (value) 1 else 0)