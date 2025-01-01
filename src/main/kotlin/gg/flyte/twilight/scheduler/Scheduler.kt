package gg.flyte.twilight.scheduler

//import com.okkero.skedule.BukkitSchedulerController
//import com.okkero.skedule.SynchronizationContext
//import com.okkero.skedule.schedule
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.time.TimeUnit
import org.bukkit.scheduler.BukkitRunnable

/**
 * Schedules a synchronous task to be executed by the Bukkit scheduler.
 *
 * @param runnable The function representing the task to be executed.
 * @return The TwilightRunnable representing the scheduled task.
 */
fun sync(runnable: TwilightRunnable.() -> Unit): TwilightRunnable {
    return TwilightRunnable(runnable, false).apply { schedule() }
}

/**
 * Schedules an asynchronous task to be executed by the Bukkit scheduler.
 *
 * @param runnable The function representing the task to be executed.
 * @return The TwilightRunnable representing the scheduled task.
 */
fun async(runnable: TwilightRunnable.() -> Unit): TwilightRunnable {
    return TwilightRunnable(runnable, true).apply { schedule() }
}

/**
 * Schedules a delayed task to be executed by the Bukkit scheduler.
 *
 * @param value The duration value for the delay.
 * @param unit The TimeUnit representing the time unit of the delay (default: MILLISECONDS).
 * @param async Whether the task should be executed asynchronously (default: false).
 * @param runnable The function representing the task to be executed.
 * @return The TwilightRunnable representing the scheduled task.
 */
fun delay(
    value: Int,
    unit: TimeUnit = TimeUnit.TICKS,
    async: Boolean = false,
    runnable: TwilightRunnable.() -> Unit
): TwilightRunnable {
    return TwilightRunnable(runnable, async, unit.toTicks(value.toLong())).apply { schedule() }
}

/**
 * A convenience wrapper around the main [delay] function, allowing you to schedule a delayed task using
 * ticks instead of specifying a time value and time unit.
 *
 * @param ticks The number of ticks for the delay (default: 1).
 * @param runnable The function representing the task to be executed.
 * @return The TwilightRunnable representing the scheduled task.
 * @see delay
 */
fun delay(ticks: Int = 1, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return delay(ticks, TimeUnit.TICKS, false, runnable)
}

/**
 * A convenience wrapper around the main [delay] method, allowing you to schedule a delayed task using
 * ticks instead of specifying a time value and time unit. Additionally, you can choose to execute the task
 * asynchronously if necessary.
 *
 * @param ticks The number of ticks for the delay (default: 1).
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The TwilightRunnable representing the scheduled task.
 * @see delay
 */
fun delay(ticks: Int = 1, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return delay(ticks, TimeUnit.TICKS, async, runnable)
}

/**
 * Schedules a repeating task to be executed by the Bukkit scheduler.
 *
 * @param delay The duration value for the initial delay.
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the delay and period (default: MILLISECONDS).
 * @param async Whether the task should be executed asynchronously (default: false).
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 */
fun repeat(
    delay: Int,
    period: Int,
    unit: TimeUnit = TimeUnit.TICKS,
    async: Boolean = false,
    runnable: BukkitRunnable.() -> Unit
): TwilightRunnable {
    return if (async) {
        TwilightRunnable(runnable, true, 0).also {
            it.runTaskTimerAsynchronously(
                Twilight.plugin,
                unit.toTicks(delay.toLong()),
                unit.toTicks(period.toLong())
            )
        }
    } else {
        TwilightRunnable(runnable, false, 0).also {
            it.runTaskTimer(
                Twilight.plugin,
                unit.toTicks(delay.toLong()),
                unit.toTicks(period.toLong())
            )
        }
    }
}

/**
 * A convenience wrapper around the main [repeat] method, allowing you to schedule a repeating task
 * using ticks instead of specifying time values and time units.
 *
 * @param periodTicks The number of ticks for the period between subsequent executions (default: 1).
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeat
 */
fun repeat(periodTicks: Int = 1, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeat(periodTicks, periodTicks, TimeUnit.TICKS, false, runnable)
}

/**
 * A convenience wrapper around the main [repeat] method, allowing you to schedule a repeating task
 * using ticks instead of specifying time values and time units. Additionally, you can choose to execute the task
 * asynchronously if necessary.
 *
 * @param periodTicks The number of ticks for the period between subsequent executions (default: 1).
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeat
 */
fun repeat(periodTicks: Int = 1, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeat(periodTicks, periodTicks, TimeUnit.TICKS, async, runnable)
}

/**
 * A convenience wrapper around the main [repeat] method, allowing you to schedule a repeating task
 * with a fixed period by specifying the period value and the time unit.
 *
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the period.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeat
 */
fun repeat(period: Int, unit: TimeUnit, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeat(period, period, unit, false, runnable)
}

/**
 * A convenience wrapper around the main [repeat] method, allowing you to schedule a repeating task
 * with a fixed period by specifying the period value, the time unit, and whether the task should be executed
 * asynchronously.
 *
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the period.
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeat
 */
fun repeat(period: Int, unit: TimeUnit, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeat(period, period, unit, async, runnable)
}

/**
 * A convenience wrapper around the main [repeat] method, allowing you to schedule a repeating task
 * with a fixed initial delay and a fixed period using ticks instead of specifying time values and time units.
 * Additionally, you can choose to execute the task asynchronously if necessary.
 *
 * @param delayTicks The number of ticks for the initial delay.
 * @param periodTicks The number of ticks for the period between subsequent executions.
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeat
 */
fun repeat(delayTicks: Int, periodTicks: Int, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeat(delayTicks, periodTicks, TimeUnit.TICKS, async, runnable)
}

/**
 * A convenience wrapper around the main [repeat] method, allowing you to schedule a repeating task
 * with a fixed initial delay and a fixed period by specifying the delay and period values and the time unit.
 *
 * @param delay The duration value for the initial delay.
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the delay and period.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeat
 */
fun repeat(delay: Int, period: Int, unit: TimeUnit, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeat(delay, period, unit, false, runnable)
}

/**
 * Schedules a repeating task to be executed by the Bukkit scheduler.
 *
 * @param delay The duration value for the initial delay.
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the delay and period (default: MILLISECONDS).
 * @param async Whether the task should be executed asynchronously (default: false).
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 */
fun repeatingTask(
    delay: Int,
    period: Int,
    unit: TimeUnit = TimeUnit.TICKS,
    async: Boolean = false,
    runnable: BukkitRunnable.() -> Unit
): TwilightRunnable {
    return if (async) {
        TwilightRunnable(runnable, true, 0).also {
            it.runTaskTimerAsynchronously(
                Twilight.plugin,
                unit.toTicks(delay.toLong()),
                unit.toTicks(period.toLong())
            )
        }
    } else {
        TwilightRunnable(runnable, false, 0).also {
            it.runTaskTimer(
                Twilight.plugin,
                unit.toTicks(delay.toLong()),
                unit.toTicks(period.toLong())
            )
        }
    }
}

/**
 * A convenience wrapper around the main [repeatingTask] method, allowing you to schedule a repeating task
 * using ticks instead of specifying time values and time units.
 *
 * @param periodTicks The number of ticks for the period between subsequent executions (default: 1).
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeatingTask
 */
fun repeatingTask(periodTicks: Int = 1, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeatingTask(periodTicks, periodTicks, TimeUnit.TICKS, false, runnable)
}

/**
 * A convenience wrapper around the main [repeatingTask] method, allowing you to schedule a repeatingTasking task
 * using ticks instead of specifying time values and time units. Additionally, you can choose to execute the task
 * asynchronously if necessary.
 *
 * @param periodTicks The number of ticks for the period between subsequent executions (default: 1).
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeatingTask
 */
fun repeatingTask(periodTicks: Int = 1, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeatingTask(periodTicks, periodTicks, TimeUnit.TICKS, async, runnable)
}

/**
 * A convenience wrapper around the main [repeatingTask] method, allowing you to schedule a repeatingTasking task
 * with a fixed period by specifying the period value and the time unit.
 *
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the period.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeatingTask
 */
fun repeatingTask(period: Int, unit: TimeUnit, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeatingTask(period, period, unit, false, runnable)
}

/**
 * A convenience wrapper around the main [repeatingTask] method, allowing you to schedule a repeatingTasking task
 * with a fixed period by specifying the period value, the time unit, and whether the task should be executed
 * asynchronously.
 *
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the period.
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeatingTask
 */
fun repeatingTask(period: Int, unit: TimeUnit, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeatingTask(period, period, unit, async, runnable)
}

/**
 * A convenience wrapper around the main [repeatingTask] method, allowing you to schedule a repeatingTasking task
 * with a fixed initial delay and a fixed period using ticks instead of specifying time values and time units.
 * Additionally, you can choose to execute the task asynchronously if necessary.
 *
 * @param delayTicks The number of ticks for the initial delay.
 * @param periodTicks The number of ticks for the period between subsequent executions.
 * @param async Whether the task should be executed asynchronously.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeatingTask
 */
fun repeatingTask(delayTicks: Int, periodTicks: Int, async: Boolean, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeatingTask(delayTicks, periodTicks, TimeUnit.TICKS, async, runnable)
}

/**
 * A convenience wrapper around the main [repeatingTask] method, allowing you to schedule a repeatingTasking task
 * with a fixed initial delay and a fixed period by specifying the delay and period values and the time unit.
 *
 * @param delay The duration value for the initial delay.
 * @param period The duration value for the period between subsequent executions.
 * @param unit The TimeUnit representing the time unit of the delay and period.
 * @param runnable The function representing the task to be executed.
 * @return The BukkitTask representing the scheduled task.
 * @see repeatingTask
 */
fun repeatingTask(delay: Int, period: Int, unit: TimeUnit, runnable: BukkitRunnable.() -> Unit): TwilightRunnable {
    return repeatingTask(delay, period, unit, false, runnable)
}

///**
// * Schedules a coroutine-based task to be executed by the Bukkit scheduler.
// *
// * @param initialContext The initial synchronization context for the task (default: `SynchronizationContext.SYNC`).
// * @param block The coroutine block representing the task to be executed.
// * @return The CoroutineTask representing the scheduled task.
// */
//fun skedule(
//    initialContext: SynchronizationContext = SynchronizationContext.SYNC,
//    block: suspend BukkitSchedulerController.() -> Unit
//) = Bukkit.getScheduler().schedule(
//    Twilight.plugin,
//    initialContext,
//    block
//)
