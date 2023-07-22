package gg.flyte.twilight.time

/**
 * Returns the English suffix for a given day of the month.
 *
 * @param day The day of the month for which to retrieve the suffix. Should be a value between 1 and 31.
 * @return The English suffix for the given day of the month.
 * @throws ArrayIndexOutOfBoundsException if the provided day is not within the valid range (1 to 31).
 */
fun getDaySuffix(day: Int): String {
    return arrayOf(
        //    0     1     2     3     4     5     6     7     8     9
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
        //    10    11    12    13    14    15    16    17    18    19
        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
        //    20    21    22    23    24    25    26    27    28    29
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
        //    30    31
        "th", "st")[day]
}