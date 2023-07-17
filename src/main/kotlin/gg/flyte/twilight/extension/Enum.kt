package gg.flyte.twilight.extension

inline fun <reified T : Enum<T>> enumValue(value: String): T? {
    return enumValues<T>().find { it.name == value.uppercase() }
}