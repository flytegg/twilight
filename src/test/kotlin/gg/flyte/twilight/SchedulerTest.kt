package gg.flyte.twilight

import gg.flyte.twilight.scheduler.async
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.scheduler.sync
import gg.flyte.twilight.scheduler.repeat
import java.util.concurrent.TimeUnit

fun main() {
    syncTaskTest()
    asyncTaskTest()
    syncTaskLaterTest()
    asyncTaskLaterTest()
    syncTaskTimerTest()
    asyncTaskTimerTest()
}

fun syncTaskTest() {
    sync {
        println("I am a sync BukkitRunnable")
    }
}

fun asyncTaskTest() {
    async {
        println("I am an async BukkitRunnable")
    }
}

fun syncTaskLaterTest() {
    delay {
        println("I am a sync BukkitRunnable delayed by 1 tick")
    }

    delay(10) {
        println("I am a sync BukkitRunnable delayed by 10 ticks")
    }

    delay(1, TimeUnit.SECONDS) {
        println("I am a sync BukkitRunnable delayed by 1 second")
    }
}

fun asyncTaskLaterTest() {
    delay(10, true) {
        println("I am an async BukkitRunnable delayed by 10 ticks")
    }

    delay(1, TimeUnit.SECONDS, true) {
        println("I am an async BukkitRunnable delayed by 1 second")
    }
}

fun syncTaskTimerTest() {
    repeat(10) {
        println("I am a sync BukkitRunnable running every 10 ticks")
    }

    repeat(10, TimeUnit.SECONDS) {
        println("I am a sync BukkitRunnable running every 10 seconds")
    }

    repeat(5, 10) {
        println("I am a sync BukkitRunnable running every 10 ticks, waiting 5 before starting")
    }

    repeat(5, 10, TimeUnit.SECONDS) {
        println("I am a sync BukkitRunnable running every 10 seconds, waiting 5 before starting")
    }
}

fun asyncTaskTimerTest() {
    repeat(10, true) {
        println("I am an async BukkitRunnable running every 10 ticks")
    }

    repeat(10, TimeUnit.SECONDS, true) {
        println("I am an asyncBukkitRunnable running every 10 seconds")
    }

    repeat(5, 10, true) {
        println("I am an async BukkitRunnable running every 10 ticks, waiting 5 before starting")
    }

    repeat(5, 10, TimeUnit.SECONDS, true) {
        println("I am an async BukkitRunnable running every 10 seconds, waiting 5 before starting")
    }
}
