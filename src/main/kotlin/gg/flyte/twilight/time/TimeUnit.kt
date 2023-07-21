package gg.flyte.twilight.time

/**
 * Provides an abstraction for representing various time units, including custom support for Minecraft ticks.
 * It allows conversion between standard time units like DAYS, HOURS, MINUTES, SECONDS, and MILLISECONDS using the
 * Java `java.util.concurrent.TimeUnit` along with custom conversions for Minecraft ticks.
 */
enum class TimeUnit(private val javaTimeUnit: java.util.concurrent.TimeUnit?) {

    DAYS(java.util.concurrent.TimeUnit.DAYS),
    HOURS(java.util.concurrent.TimeUnit.HOURS),
    MINUTES(java.util.concurrent.TimeUnit.MINUTES),
    SECONDS(java.util.concurrent.TimeUnit.SECONDS),
    TICKS(null),
    MILLISECONDS(java.util.concurrent.TimeUnit.MILLISECONDS);

    companion object {
        private const val TICKS_PER_SECOND = 20
        private const val TICKS_PER_MINUTE = TICKS_PER_SECOND * 60
        private const val TICKS_PER_HOUR = TICKS_PER_MINUTE * 60
        private const val TICKS_PER_DAY = TICKS_PER_HOUR * 24
    }

    /**
     * Convert the given `duration` to days.
     *
     * @param duration The duration to be converted.
     * @return The equivalent number of days.
     */
    fun toDays(duration: Long): Long {
        return javaTimeUnit?.toDays(duration) ?: (duration / TICKS_PER_DAY)
    }

    /**
     * Convert the given `duration` to hours.
     *
     * @param duration The duration to be converted.
     * @return The equivalent number of hours.
     */
    fun toHours(duration: Long): Long {
        return javaTimeUnit?.toHours(duration) ?: (duration / TICKS_PER_HOUR)
    }

    /**
     * Convert the given `duration` to minutes.
     *
     * @param duration The duration to be converted.
     * @return The equivalent number of minutes.
     */
    fun toMinutes(duration: Long): Long {
        return javaTimeUnit?.toMinutes(duration) ?: (duration / TICKS_PER_MINUTE)
    }

    /**
     * Convert the given `duration` to seconds.
     *
     * @param duration The duration to be converted.
     * @return The equivalent number of seconds.
     */
    fun toSeconds(duration: Long): Long {
        return javaTimeUnit?.toSeconds(duration) ?: (duration / TICKS_PER_SECOND)
    }

    /**
     * Convert the given `duration` to Minecraft ticks.
     *
     * @param duration The duration to be converted.
     * @return The equivalent number of Minecraft ticks.
     */
    fun toTicks(duration: Long): Long {
        return (javaTimeUnit?.toMillis(duration)?.div(50)) ?: duration
    }

    /**
     * Convert the given `duration` to milliseconds.
     *
     * @param duration The duration to be converted.
     * @return The equivalent number of milliseconds.
     */
    fun toMillis(duration: Long): Long {
        return javaTimeUnit?.toMillis(duration) ?: (duration * 50)
    }

}