package gg.flyte.twilight.scheduler

import gg.flyte.twilight.Twilight
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit

fun sync(runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return Twilight.plugin.server.scheduler.runTask(Twilight.plugin, createBukkitRunnable(runnable))
}

fun async(runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return Twilight.plugin.server.scheduler.runTaskAsynchronously(Twilight.plugin, createBukkitRunnable(runnable))
}

fun delay(value: Int, unit: TimeUnit = TimeUnit.MILLISECONDS, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return Twilight.plugin.server.scheduler.runTaskLater(Twilight.plugin, createBukkitRunnable(runnable), unit.toMillis(value.toLong()) / 50)
}

fun repeat(delay: Int, period: Int, async: Boolean = false, unit: TimeUnit = TimeUnit.MILLISECONDS, runnable: Runnable.() -> Unit): BukkitTask {
    return if (async) {
        return Twilight.plugin.server.scheduler.runTaskTimerAsynchronously(Twilight.plugin, createBukkitRunnable(runnable), unit.toMillis(delay.toLong()) / 50, unit.toMillis(period.toLong()) / 50)
    } else {
        return Twilight.plugin.server.scheduler.runTaskTimer(Twilight.plugin, createBukkitRunnable(runnable), unit.toMillis(delay.toLong()) / 50, unit.toMillis(period.toLong()) / 50)
    }
}

fun repeat(delay: Int, period: Int, unit: TimeUnit = TimeUnit.MILLISECONDS, async: Boolean = false, runnable: Runnable.() -> Unit): BukkitTask {
    return repeat(delay, period, async, unit, runnable)
}

fun createBukkitRunnable(runnable: BukkitRunnable.() -> Unit): BukkitRunnable {
    return object : BukkitRunnable() {
        override fun run() {
            this.runnable()
        }
    }
}