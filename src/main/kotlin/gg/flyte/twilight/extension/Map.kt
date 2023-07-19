package gg.flyte.twilight.extension

fun <K, V> Map<K, V>.findKeyByValue(value: V): K? {
    return entries.find { it.value == value }?.key
}

fun <K, V> Map<K, V>.findKeysByValue(value: V): List<K> {
    return entries.filter { it.value == value }.map { it.key }
}