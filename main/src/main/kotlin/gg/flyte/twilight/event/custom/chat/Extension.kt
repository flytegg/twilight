package gg.flyte.twilight.event.custom.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.md_5.bungee.api.chat.TextComponent

/**
 * Attaches a custom click event to a Component.
 *
 * This function adds a click event to the Component that triggers a command
 * when the component is clicked in the chat. The command is constructed by
 * prefixing '/chatclick' with the joined string representations of the provided
 * vararg data parameters, separated by spaces. This will then trigger the
 * ChatClickEvent which will contain the data.
 *
 * @param data Vararg parameter strings that are concatenated and used as data for the command.
 * @return The Component with the attached click event.
 */
fun Component.customClickEvent(vararg data: String): Component {
    return this.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/chatclick ${data.joinToString(" ")}"))
}

/**
 * Attaches a custom click event to a TextComponent.
 *
 * This function assigns a new click event to the TextComponent that will execute a command
 * when the component is interacted with in the chat. The command consists of '/chatclick'
 * followed by the space-separated concatenation of the input data. This will then trigger
 * the ChatClickEvent which will contain the data.
 *
 * @param data Vararg parameter strings that are combined into a single command string.
 * @return The TextComponent with the new click event.
 */
fun TextComponent.customClickEvent(vararg data: String): TextComponent {
    clickEvent = net.md_5.bungee.api.chat.ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/chatclick ${data.joinToString(" ")}")
    return this
}