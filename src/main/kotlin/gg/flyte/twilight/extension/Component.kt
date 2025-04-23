package gg.flyte.twilight.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * Generates a Component that visually represents a solid line.
 *
 * The solid line is the perfect width to fit a default Minecraft chat bar.
 * It is composed of whitespace characters, which are then decorated with
 * a strikethrough, giving the appearance of a solid line.
 *
 * @return A TextComponent that visually represents a solid line.
 */
fun Component.solidLine(): TextComponent {
    return Component.text("                                                                               ").decorate(TextDecoration.STRIKETHROUGH)
}

fun Component.asString(): String {
   return MiniMessage.miniMessage().serialize(this)
}

/*
Converts Strings to Adventure Component

The approach initially attempts to parse with Minimessage, and if that fails
It falls back to Legacy Serialiser for both Ampersand and Section.
 */

fun String.toComponent(): Component {
    return runCatching {
        MiniMessage.miniMessage().deserialize(this)
    }.recoverCatching {
        LegacyComponentSerializer.legacyAmpersand().deserialize(this)
    }.recoverCatching {
        LegacyComponentSerializer.legacySection().deserialize(this)
    }.getOrElse {
        Component.text(this)
    }
}