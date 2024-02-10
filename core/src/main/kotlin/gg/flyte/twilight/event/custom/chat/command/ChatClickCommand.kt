package gg.flyte.twilight.event.custom.chat.command

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.event.custom.chat.ChatClickEvent
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

object ChatClickCommand : BukkitCommand("chatclick") {

    fun register() {
        runCatching {
            val commandMapField = Twilight.plugin.server.javaClass.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
            val commandMap = commandMapField.get(Twilight.plugin.server) as CommandMap
            commandMap.register("chatclick", this@ChatClickCommand)
        }
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) return false
        Twilight.plugin.server.pluginManager.callEvent(ChatClickEvent(sender, args))
        return false
    }

}