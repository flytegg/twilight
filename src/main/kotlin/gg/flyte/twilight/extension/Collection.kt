package gg.flyte.twilight.extension

/**
 * Applies the specified action to each element in the iterable.
 *
 * This extension function iterates through the elements of the iterable and
 * applies the given action to each element, allowing for in-place modification
 * or transformation of the elements.
 *
 * @param action The lambda function to apply to each element.
 */
inline fun <T> Iterable<T>.applyForEach(action: T.() -> Unit) {
    forEach { it.action() }
}
