package gg.flyte.twilight.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }

    fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

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

    fun formatDate(date: LocalDate, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(formatter)
    }

    fun parseDate(dateString: String, pattern: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDate.parse(dateString, formatter)
    }

    fun formatDateTime(dateTime: LocalDateTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }

    fun parseDateTime(dateTimeString: String, pattern: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    fun getDaysDifference(startDate: LocalDate, endDate: LocalDate): Long {
        return endDate.toEpochDay() - startDate.toEpochDay()
    }

    fun formatDateWithPattern(date: LocalDate, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(formatter)
    }
    // dd-MM-yyyy sorta thing
    fun formatDateTimeWithPattern(dateTime: LocalDateTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }

    fun getFormattedTime(t: Long, div: Int): String {
        var seconds = t / div // 86400
        val hours = seconds / 3600 // 24
        seconds -= hours * 3600 // 86400 - 84600 = 0
        val minutes = seconds / 60 // 0
        seconds -= minutes * 60 // 59 * 60 = 3540
        return (if (hours != 0L) "${hours}h " else "") + zeroed(minutes) + "m " + zeroed(seconds) + "s"
    }

    fun getFormattedTicks(ticks: Long): String {
        return getFormattedTime(ticks, 20)
    }
    fun getFormattedMillis(millis: Long): String {
        return getFormattedTime(millis, 1000)
    }

    fun zeroed(l: Long): String {
        return if (l > 9) "" + l else "0$l"
    }
}