package gg.flyte.twilight.scheduler

import gg.flyte.twilight.Twilight
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit

fun sync(runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return createBukkitRunnable(runnable).runTask(Twilight.plugin)
}

fun async(runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return createBukkitRunnable(runnable).runTaskAsynchronously(Twilight.plugin)
}

fun delay(
    value: Int,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    async: Boolean = false,
    runnable: BukkitRunnable.() -> Unit
): BukkitTask {
    return if (async) {
        createBukkitRunnable(runnable).runTaskLaterAsynchronously(Twilight.plugin, unit.toMillis(value.toLong()) / 50)
    } else {
        createBukkitRunnable(runnable).runTaskLater(Twilight.plugin, unit.toMillis(value.toLong()) / 50)
    }
}

fun delay(ticks: Int = 1, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return delay(ticks, TimeUnit.MILLISECONDS, false, runnable)
}

fun delay(ticks: Int = 1, async: Boolean, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return delay(ticks, TimeUnit.MILLISECONDS, async, runnable)
}

fun repeat(
    delay: Int,
    period: Int,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    async: Boolean = false,
    runnable: BukkitRunnable.() -> Unit
): BukkitTask {
    return if (async) {
        createBukkitRunnable(runnable).runTaskTimerAsynchronously(
            Twilight.plugin,
            unit.toMillis(delay.toLong()) / 50,
            unit.toMillis(period.toLong()) / 50
        )
    } else {
        createBukkitRunnable(runnable).runTaskTimer(
            Twilight.plugin,
            unit.toMillis(delay.toLong()) / 50,
            unit.toMillis(period.toLong()) / 50
        )
    }
}

fun repeat(period: Int, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return repeat(period, period, TimeUnit.MILLISECONDS, false, runnable)
}

fun repeat(period: Int, async: Boolean, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return repeat(period, period, TimeUnit.MILLISECONDS, async, runnable)
}

fun repeat(period: Int, unit: TimeUnit, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return repeat(period, period, unit, false, runnable)
}

fun repeat(period: Int, unit: TimeUnit, async: Boolean, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return repeat(period, period, unit, async, runnable)
}

fun repeat(delay: Int, period: Int, async: Boolean, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return repeat(delay, period, TimeUnit.MILLISECONDS, async, runnable)
}

fun repeat(delay: Int, period: Int, unit: TimeUnit, runnable: BukkitRunnable.() -> Unit): BukkitTask {
    return repeat(delay, period, unit, false, runnable)
}

private fun createBukkitRunnable(runnable: BukkitRunnable.() -> Unit): BukkitRunnable {
    return object : BukkitRunnable() {
        override fun run() {
            this.runnable()
        }
    }
}