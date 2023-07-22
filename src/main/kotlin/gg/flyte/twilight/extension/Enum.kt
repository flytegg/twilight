package gg.flyte.twilight.extension

/**
 * Returns the enum constant of the specified enum type `T` that matches the given `value`.
 *
 * This inline function takes a string `value` and attempts to find the corresponding enum constant
 * of the specified enum class `T`. The enum class must be passed as a reified type parameter, allowing
 * the function to access the enum values and perform a case-insensitive search to match the `value`.
 *
 * @param T The enum class type for which to find the matching enum constant.
 * @param value The string value representing the name of the desired enum constant.
 * @return The enum constant of type `T` that matches the given `value`, or null if no match is found.
 * @throws IllegalArgumentException if `T` is not an enum class type.
 */
inline fun <reified T : Enum<T>> enumValue(value: String): T? {
    return enumValues<T>().find { it.name == value.uppercase() }
}