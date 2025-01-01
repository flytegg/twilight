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
