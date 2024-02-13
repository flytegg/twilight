package gg.flyte.twilight.time

/**
 * Returns the English suffix for a given day of the month.
 *
 * @param day The day of the month for which to retrieve the suffix.
 * @return The English suffix for the given day.
 */
fun getDaySuffix(day: Int): String {
    if (day in 11..13) return "th"

    val lastTwoDigits = day % 100
    val suffix = when (lastTwoDigits) {
        11, 12, 13 -> "th"
        else -> {
            val lastDigit = day % 10
            when (lastDigit) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }
    }

    return suffix
}
