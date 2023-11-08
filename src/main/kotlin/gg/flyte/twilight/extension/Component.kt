package gg.flyte.twilight.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration

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