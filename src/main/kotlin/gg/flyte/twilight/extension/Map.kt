package gg.flyte.twilight.extension

/**
 * Finds the first key associated with the specified value in the map.
 *
 * This method searches for the first key in the map that is associated with the given `value`.
 * If a matching key is found, it is returned. Otherwise, the method returns null. The search
 * is performed by iterating through the map's entries and comparing the values.
 *
 * @param value The value to search for in the map.
 * @return The first key associated with the specified value, or null if no such key is found.
 */
fun <K, V> Map<K, V>.findKeyByValue(value: V): K? {
    return entries.find { it.value == value }?.key
}

/**
 * Finds all keys associated with the specified value in the map.
 *
 * This method searches for all keys in the map that are associated with the given `value`.
 * It returns a list containing all matching keys found in the map. If no keys are associated
 * with the specified value, an empty list is returned. The search is performed by iterating
 * through the map's entries and filtering the keys based on the values.
 *
 * @param value The value to search for in the map.
 * @return A list of keys associated with the specified value, or an empty list if no such keys are found.
 */
fun <K, V> Map<K, V>.findKeysByValue(value: V): List<K> {
    return entries.filter { it.value == value }.map { it.key }
}
