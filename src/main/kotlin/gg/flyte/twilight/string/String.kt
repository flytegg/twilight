package gg.flyte.twilight.string

import org.bukkit.ChatColor
import java.math.BigInteger
import java.util.*

/**
 * Strips color codes from the string.
 *
 * This method removes color codes from the string. If the string contains color codes
 * introduced by Bukkit's `ChatColor` class, this method removes them and returns the
 * string without color codes. If the string has no color codes, it returns the original
 * string as is.
 *
 * @return The string with color codes stripped, or the original string if no color codes found.
 */
fun String.stripColor(): String {
    return ChatColor.stripColor(this) ?: this
}

/**
 * Translates color codes in the string.
 *
 * This method translates color codes in the string using the ampersand (&) character
 * as the color code indicator. It replaces color codes with their corresponding color
 * representations in the provided string using Bukkit's `ChatColor` class.
 *
 * @return The translated string with color codes replaced by their corresponding colors.
 */
fun String.translate(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

/**
 * Translates color codes in the list of strings.
 *
 * This method translates color codes in each string within the list using the ampersand (&)
 * character as the color code indicator. It replaces color codes with their corresponding
 * color representations in each string using Bukkit's `ChatColor` class. The method returns
 * a new list with the translated strings.
 *
 * @return The list of strings with color codes replaced by their corresponding colors.
 */
fun List<String>.translate(): List<String> {
    return map { it.translate() }
}

/**
 * Converts the string to small caps text, optionally translating color codes first.
 *
 * This method converts the string to small caps text by replacing uppercase letters with
 * their corresponding small caps Unicode characters. If the `translate` parameter is set
 * to `true`, it will first translate any color codes in the string using the `translate()`
 * method.
 *
 * @param translate Determines whether to translate color codes before converting to small caps.
 *                  Defaults to `false`.
 * @return The string converted to small caps text with optional color code translation.
 */
fun String.smallText(translate: Boolean = false): String {
    var string = this
    if (translate) string = translate()
    return string.uppercase()
        .replace("Q", "ꞯ")
        .replace("W", "ᴡ")
        .replace("E", "ᴇ")
        .replace("R", "ʀ")
        .replace("T", "ᴛ")
        .replace("Y", "ʏ")
        .replace("U", "ᴜ")
        .replace("I", "ɪ")
        .replace("O", "ᴏ")
        .replace("P", "ᴘ")
        .replace("A", "ᴀ")
        .replace("S", "ѕ")
        .replace("D", "ᴅ")
        .replace("F", "ꜰ")
        .replace("G", "ɢ")
        .replace("H", "ʜ")
        .replace("J", "ᴊ")
        .replace("K", "ᴋ")
        .replace("L", "ʟ")
        .replace("Z", "ᴢ")
        .replace("X", "x")
        .replace("C", "ᴄ")
        .replace("V", "ᴠ")
        .replace("B", "ʙ")
        .replace("N", "ɴ")
        .replace("M", "ᴍ")
}

/**
 * Converts the string to a UUID.
 *
 * This method converts the string representation of a UUID into a `java.util.UUID` object.
 * If the input string already contains hyphens ("-"), it assumes that the string is a valid
 * UUID representation and returns the corresponding UUID using `UUID.fromString()`.
 *
 * If the input string does not contain hyphens, it assumes that the string is a compact UUID
 * representation without hyphens, and it constructs the UUID from the two halves of the compact
 * representation.
 *
 * @return The UUID representation of the string.
 * @throws IllegalArgumentException if the input string is not a valid UUID representation.
 */
fun String.toUUID(): UUID {
    if (contains("-")) return UUID.fromString(this)
    return UUID(BigInteger(substring(0, 16), 16).toLong(), BigInteger(substring(16, 32), 16).toLong())
}

/**
 * A map of irregular nouns to their plural forms.
 */
val IRREGULAR_NOUNS = mapOf(
    "man" to "men",
    "woman" to "women",
    "child" to "children",
    "tooth" to "teeth",
    "foot" to "feet",
    "mouse" to "mice",
    "person" to "people",
    "goose" to "geese",
    "ox" to "oxen",
    "leaf" to "leaves",
    "sheep" to "sheep",
    "deer" to "deer",
    "fish" to "fish",
    "moose" to "moose",
    "aircraft" to "aircraft",
    "hovercraft" to "hovercraft",
    "spacecraft" to "spacecraft",
    "watercraft" to "watercraft",
    "offspring" to "offspring",
    "species" to "species",
    "series" to "series",
)

/**
 * Pluralizes a singular noun.
 *
 * This function converts a singular noun to its plural form according to English grammar rules,
 * including handling of irregular nouns, regular nouns ending with 'y', Latin-origin nouns ending with 'us',
 * Greek-origin nouns ending with 'is', and nouns ending with 'ch', 'sh', 'x', 's', or 'z'.
 *
 * @return The plural form of the noun.
 */
fun String.pluralize(): String = IRREGULAR_NOUNS[this]?.let { return it } ?: when {
    endsWith("y") && length > 1 && !this[lastIndex - 1].isVowel() -> dropLast(1) + "ies"
    endsWith("us") -> dropLast(2) + "i"  // Latin origin, e.g., "cactus" to "cacti"
    endsWith("is") -> dropLast(2) + "es" // Greek origin, e.g., "analysis" to "analyses"
    endsWith("ch") || endsWith("sh") || endsWith("x") || endsWith("s") || endsWith("z") -> this + "es"
    else -> this + "s"
}

/**
 * A list of English vowels.
 */
var VOWELS = listOf('a', 'e', 'i', 'o', 'u')

/**
 * Checks if a character is a vowel.
 *
 * Vowels are 'a', 'e', 'i', 'o', and 'u'. This function checks if the character (case-insensitive) is one of these vowels.
 *
 * @return `true` if the character is a vowel, `false` otherwise.
 */
fun Char.isVowel(): Boolean = lowercaseChar() in VOWELS

var CASE_DELIMITER_REGEX = Regex("(?<!^)(?=[A-Z])|[_\\-\\s]+")

/**
 * Splits a string into words based on capital letters, underscores, hyphens, and spaces, then converts it to lowerCamelCase.
 *
 * The first word is converted to lowercase, and each subsequent word is capitalized and concatenated to form a single string.
 *
 * @return The string converted to lowerCamelCase.
 */
fun String.lowerCamelCase(): String = CASE_DELIMITER_REGEX.split(this)
    .filter { it.isNotEmpty() }
    .mapIndexed { index, word ->
        if (index == 0) word.lowercase(Locale.getDefault())
        else word.capitalizeFirstLetter()
    }.joinToString("")

/**
 * Capitalizes the first letter of a string.
 *
 * If the first character of the string is lowercase, it is converted to uppercase. Otherwise, the string is returned unchanged.
 *
 * @return The string with its first letter capitalized.
 */
fun String.capitalizeFirstLetter(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}