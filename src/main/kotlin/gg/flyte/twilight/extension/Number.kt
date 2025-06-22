package gg.flyte.twilight.extension

import java.math.RoundingMode
import java.text.DecimalFormat

fun Number.format(): String {
    val arr = arrayOf("", "k", "M", "B", "T")
    var index = 0
    var value = this.toDouble()

    while (value >= 1000 && index < arr.size - 1) {
        value /= 1000
        index++
    }

    val decimalFormat = if (value < 1000000) DecimalFormat("#") else DecimalFormat("#.#")
    var formattedValue = decimalFormat.format(value)

    if (formattedValue.endsWith(".")) formattedValue = formattedValue.dropLast(1)

    return "$formattedValue${arr[index]}"
}

fun Number.round(): Number {
    return kotlin.math.round(this.toDouble())
}

fun Number.round(places: Int): Number {
    return this.toDouble().toBigDecimal().setScale(places, RoundingMode.HALF_UP).toDouble()
}

/**
 * Checks if a number is whole (has no decimal part)
 * @return true if the number is whole, false otherwise
 */
fun Number.isWhole(): Boolean = this.toDouble() == this.toLong().toDouble()

/**
 * Checks if a number is positive (greater than zero)
 * @return true if the number is positive, false otherwise
 */
fun Number.isNonNegative(): Boolean = this.toDouble() >= 0.0

/**
 * Validates that a number is whole and positive
 * @throws IllegalArgumentException if the number is not whole or not positive
 * @return the number as a Long if it passes validation
 */
fun Number.requireWholeAndPositive(): Long {
    require(this.isWhole()) { "Number must be whole (no decimal part)" }
    require(this.isNonNegative()) { "Number must be positive" }
    return this.toLong()
}
