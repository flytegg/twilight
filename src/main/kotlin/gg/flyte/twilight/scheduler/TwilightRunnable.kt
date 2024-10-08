package gg.flyte.twilight.scheduler

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.time.TimeUnit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * A flexible runnable wrapper class for Bukkit/Spigot that supports chaining of tasks with
 * optionally set delays and sync/async execution
 *
 * @param task The task context to be executed by this runnable
 * @param async Whether this task should be run async
 * @param delay The delay before this task should be executed in ticks
 */
class TwilightRunnable(
    private val task: TwilightRunnable.() -> Unit,
    val async: Boolean,
    private val delay: Long = 0
) : BukkitRunnable() {

    private var nextRunnable: TwilightRunnable? = null

    // Executes main task and schedules the next runnable if one exists
    override fun run() {
        task()
        nextRunnable?.schedule()
    }

    /**
     * Define a runnable to executed after the completion of this runnable,
     * This runnable will run the same async state of the previous runnable
     * @param delay delay before this task should be executed
     * @param unit The unit of time the delay is in
     * @param action The task to be executed
     * @return The new TwilightRunnable
     */
    fun onComplete(
        delay: Int = 0,
        unit: TimeUnit = TimeUnit.TICKS,
        action: TwilightRunnable.() -> Unit
    ): TwilightRunnable {
        return chainRunnable(action, async, unit.toTicks(delay.toLong()))
    }

    /**
     * Define a runnable to executed synchronously after the completion of this runnable
     * @param delay delay before this task should be executed
     * @param unit The unit of time the delay is in
     * @param action The task to be executed
     * @return The new TwilightRunnable
     */
    fun onCompleteSync(
        delay: Int = 0,
        unit: TimeUnit = TimeUnit.TICKS,
        action: TwilightRunnable.() -> Unit
    ): TwilightRunnable {
        return chainRunnable(action, false, unit.toTicks(delay.toLong()))
    }

    /**
     * Define a runnable to executed asynchronously after the completion of this runnable
     * @param delay delay before this task should be executed
     * @param unit The unit of time the delay is in
     * @param action The task to be executed
     * @return The new TwilightRunnable
     */
    fun onCompleteAsync(
        delay: Int = 0,
        unit: TimeUnit = TimeUnit.TICKS,
        action: TwilightRunnable.() -> Unit
    ): TwilightRunnable {
        return chainRunnable(action, true, unit.toTicks(delay.toLong()))
    }

    /**
     * Chains a new task to be executed after the current one completes
     * @param action The task to be executed
     * @param async Whether the new task is async
     * @param delay The delay before the new task should execute in ticks
     * @return The new TwilightRunnable
     */
    private fun chainRunnable(action: TwilightRunnable.() -> Unit, async: Boolean, delay: Long = 0): TwilightRunnable {
        val newRunnable = TwilightRunnable(action, async, delay)
        if (nextRunnable == null) {
            nextRunnable = newRunnable
        } else {
            var last = nextRunnable
            while (last?.nextRunnable != null) {
                last = last.nextRunnable
            }
            last?.nextRunnable = newRunnable
        }
        return newRunnable
    }

    /**
     * Schedules this runnable to be executed
     * TODO Repeating runnables cause issues with onComplete as it attempts to schedule identical tasks
     * TODO Will fix at some point but for now just dont onComplete on repeat, it's pretty pointless to do so anyway
     * TODO For now, I have not implemented this class for repeat in Scheduler.kt to prevent people using it
     *
     * @param delay Additional delay to be added in ticks
     * @return The BukkitTask
     */
    fun schedule(delay: Int = 0): BukkitTask {
        val totalDelay = this.delay + delay
        return if (async) {
            if (totalDelay > 0) this.runTaskLaterAsynchronously(Twilight.plugin, totalDelay)
            else this.runTaskAsynchronously(Twilight.plugin)
        } else {
            if (totalDelay > 0) this.runTaskLater(Twilight.plugin, totalDelay)
            else this.runTask(Twilight.plugin)
        }
    }
}