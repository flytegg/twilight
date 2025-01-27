package gg.flyte.twilight.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

/**
 * Class to help manage Scoreboards and stuff related to them so:
 * - Sidebar
 * - BelowName
 * - Prefix/Suffix
 * - TabList (or PlayerList)
 */
class TwilightScoreboard(private val player: Player) {
    private var scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private var sidebarObjective: Objective? = null
    private var belowNameObjective: Objective? = null
    private val teams = mutableMapOf<String, Team>()

    init {
        player.scoreboard = scoreboard
    }

    /**
     * Updates the title of the sidebar objective.
     * Creates a new sidebar objective if one does not exist.
     *
     * @param title The text component to use as the sidebar title
     */
    fun updateSidebarTitle(title: Component) {
        if (sidebarObjective == null) {
            sidebarObjective = scoreboard.registerNewObjective("sidebar", "dummy", title)
            sidebarObjective?.displaySlot = DisplaySlot.SIDEBAR
        } else {
            sidebarObjective?.displayName(title)
        }
    }

    /**
     * Refreshes all lines of the sidebar with the one passed as arguments.
     *
     * @param lines Text components you want to have as lines.
     */
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

    /**
     * Updates the player list (tab) header and footer.
     *
     * @param header Components you want to use in the header
     * @param footer Components you want to use in the footer
     */
    fun updateTabList(header: () -> Component, footer: () -> Component) {
        player.sendPlayerListHeaderAndFooter(
            header(),
            footer()
        )
    }

    /**
     * Creates or updates the title displayed below player names.
     * Sets a custom title for the below-name display.
     *
     * @param title The text component to display after the score
     */
    fun belowName(title: Component) {
        if (belowNameObjective == null) {
            belowNameObjective = scoreboard.registerNewObjective("belowname", "dummy", title)
            belowNameObjective?.displaySlot = DisplaySlot.BELOW_NAME
        } else {
            belowNameObjective?.displayName(title)
        }
    }

    /**
     * Sets the score of the BelowName objective.
     *
     * @param target The player you want to update the score to.
     * @param score The int value you want to have as score
     */
    fun updateBelowNameScore(target: Player, score: Int) {
        belowNameObjective?.getScore(target.name)?.score = score
    }

    /**
     * Adds a suffix to a specific player's name.
     * *Note*: Only the player tied to the specific scoreboard instance will be able
     * to see the prefix.
     *
     * @param target The player whose name will have a prefix
     * @param prefix The text component to display before the player's name
     */
    fun prefix(target: Player, prefix: Component) {
        val teamName = "prefix_${target.uniqueId}"
        val team = teams.getOrPut(teamName) {
            scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        }
        team.prefix(prefix)
        team.addEntry(target.name)
    }

    /**
     * Adds a suffix to a specific player's name.
     * *Note*: Only the player tied to the specific scoreboard instance will be able
     * to see the suffix.
     *
     * @param target The player whose name will have a suffix
     * @param suffix The text component to display after the player's name
     */
    fun suffix(target: Player, suffix: Component) {
        val teamName = "prefix_${target.uniqueId}"
        val team = teams.getOrPut(teamName) {
            scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        }
        team.suffix(suffix)
        team.addEntry(target.name)
    }

    /**
     * Returns the player associated with this scoreboard.
     *
     * @return The Bukkit Player instance
     */
    fun player(): Player = player

    /**
     * Resets the player's scoreboard to the default and cleans up all custom objectives and teams.
     */
    fun delete() {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        sidebarObjective?.unregister()
        belowNameObjective?.unregister()
        teams.values.forEach { it.unregister() }
        teams.clear()
    }
}