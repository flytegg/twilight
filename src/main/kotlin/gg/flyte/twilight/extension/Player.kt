package gg.flyte.twilight.extension

import org.bukkit.Sound
import org.bukkit.entity.Player

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