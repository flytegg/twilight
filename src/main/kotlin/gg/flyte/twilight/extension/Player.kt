package gg.flyte.twilight.extension

import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.playSound(sound: Sound) {
    playSound(location, sound, 1.0F, 1.0F)
}

