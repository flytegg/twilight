package gg.flyte.twilight.extension

fun <K, V> Map<K, V>.findKeyByValue(value: V): K? {
    return entries.find { it.value == value }?.key
}