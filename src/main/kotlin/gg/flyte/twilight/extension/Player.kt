package gg.flyte.twilight.extension

import gg.flyte.twilight.Twilight
import net.kyori.adventure.text.Component.text
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Plays a sound at the player's current location with default volume and pitch.
 *
 * This method plays the specified sound at the player's current location. The sound will be audible
 * to the player at full volume and pitch.
 *
 * @param sound The Sound enum representing the sound to be played.
 */
fun Player.playSound(sound: Sound) {
    playSound(location, sound, 1.0F, 1.0F)
}

/**
 * Sends a message to the player's action bar.
 *
 * @param message The message to be displayed on the action bar.
 */
fun Player.sendActionBar(message: String) {
    spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
}

/**
 * Removes any existing action bar for the player.
 */
fun Player.clearActionBar() {
    spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(""))
}

/**
 * Restores the player's food level to maximum.
 *
 * This method sets the player's food level to its maximum value of 20, effectively
 * filling the player's hunger bar completely. The player will be fully fed after
 * calling this method.
 */
fun Player.feed() { foodLevel = 20 }
fun Player.resetWalkSpeed() { walkSpeed = 0.2F }
fun Player.resetFlySpeed() { flySpeed = 0.1F}

/**
 * Adds the specified [itemStack] to the player's inventory and/or drops remaining on the ground once inventory is full.
 *
 * @param itemStack The ItemStack to be added to the player's inventory.
 */
fun Player.addToInvOrDrop(itemStack: ItemStack) {
    inventory.addItem(itemStack).values.forEach {
        location.dropItem(itemStack)
    }
}

/**
 * Hides the player from all online players.
 */
fun Player.hidePlayer() {
    Bukkit.getOnlinePlayers().forEach {
        it.hidePlayer(Twilight.plugin, this)
    }
}

/**
 * Shows the player to all online players.
 */
fun Player.showPlayer() {
    Bukkit.getOnlinePlayers().forEach {
        it.showPlayer(Twilight.plugin, this)
    }
}

/**
 * Clears all the player's active potion effects
 */
fun Player.removeActivePotionEffects() {
    activePotionEffects.forEach { removePotionEffect(it.type) }
}