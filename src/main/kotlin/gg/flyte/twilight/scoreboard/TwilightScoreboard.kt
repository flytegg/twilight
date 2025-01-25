package gg.flyte.twilight.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

class TwilightScoreboard(private val player: Player) {
    private var scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private var sidebarObjective: Objective? = null
    private var belowNameObjective: Objective? = null
    private val teams = mutableMapOf<String, Team>()

    init {
        player.scoreboard = scoreboard
    }

    fun updateSidebarTitle(title: Component) {
        if (sidebarObjective == null) {
            sidebarObjective = scoreboard.registerNewObjective("sidebar", "dummy", title)
            sidebarObjective?.displaySlot = DisplaySlot.SIDEBAR
        } else {
            sidebarObjective?.displayName(title)
        }
    }

    fun updateSidebarLines(vararg lines: Component) {
        if (sidebarObjective == null) return

        scoreboard.entries.forEach { entry ->
            if (entry.startsWith(" ")) {
                scoreboard.resetScores(entry)
            }
        }

        lines.forEachIndexed { index, line ->
            val score = lines.size - index
            val entry = " ".repeat(score)

            val team = teams.getOrPut("line_$index") {
                scoreboard.getTeam("line_$index") ?: scoreboard.registerNewTeam("line_$index").apply {
                    addEntry(entry)
                }
            }

            team.prefix(line)
            sidebarObjective?.getScore(entry)?.score = score
        }
    }

    fun updateTabList(header: () -> Component, footer: () -> Component) {
        player.sendPlayerListHeaderAndFooter(
            header(),
            footer()
        )
    }

    fun belowName(title: Component) {
        if (belowNameObjective == null) {
            belowNameObjective = scoreboard.registerNewObjective("belowname", "dummy", title)
            belowNameObjective?.displaySlot = DisplaySlot.BELOW_NAME
        } else {
            belowNameObjective?.displayName(title)
        }
    }

    fun updateBelowNameScore(target: Player, score: Int) { belowNameObjective?.getScore(target.name)?.score = score }

    fun prefix(target: Player, prefix: Component) {
        val teamName = "prefix_${target.uniqueId}"
        val team = teams.getOrPut(teamName) {
            scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        }
        team.prefix(prefix)
        team.addEntry(target.name)
    }

    fun suffix(target: Player, suffix: Component) {
        val teamName = "prefix_${target.uniqueId}"
        val team = teams.getOrPut(teamName) {
            scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        }
        team.suffix(suffix)
        team.addEntry(target.name)
    }

    fun player(): Player = player

    fun delete() {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        sidebarObjective?.unregister()
        belowNameObjective?.unregister()
        teams.values.forEach { it.unregister() }
        teams.clear()
    }
}