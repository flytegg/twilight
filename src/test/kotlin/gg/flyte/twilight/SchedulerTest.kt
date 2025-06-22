package gg.flyte.twilight

import gg.flyte.twilight.scheduler.async
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.scheduler.repeat
import gg.flyte.twilight.scheduler.sync
import gg.flyte.twilight.time.TimeUnit

fun main() {
    syncTaskTest()
    asyncTaskTest()
    syncTaskLaterTest()
    asyncTaskLaterTest()
    syncTaskTimerTest()
    asyncTaskTimerTest()
    numberValidationTest()
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

/**
 * Tests that the scheduler methods accept different Number types and validate that they are whole and positive.
 */
fun numberValidationTest() {
    println("\n--- Number Validation Tests ---")

    // Test with different Number types (should work with whole, positive numbers)
    try {
        delay(5) { println("Integer delay works") }
        println("✅ Integer delay test passed")
    } catch (e: Exception) {
        println("❌ Integer delay test failed: ${e.message}")
    }

    try {
        delay(5.0) { println("Double delay works") }
        println("✅ Double delay test passed")
    } catch (e: Exception) {
        println("❌ Double delay test failed: ${e.message}")
    }

    try {
        delay(5.0f) { println("Float delay works") }
        println("✅ Float delay test passed")
    } catch (e: Exception) {
        println("❌ Float delay test failed: ${e.message}")
    }

    // Test with non-whole numbers (should fail)
    try {
        delay(5.5) { println("This should not execute") }
        println("❌ Non-whole number test failed: accepted 5.5")
    } catch (e: IllegalArgumentException) {
        println("✅ Non-whole number test passed: ${e.message}")
    }

    // Test with negative numbers (should fail)
    try {
        delay(-5) { println("This should not execute") }
        println("❌ Negative number test failed: accepted -5")
    } catch (e: IllegalArgumentException) {
        println("✅ Negative number test passed: ${e.message}")
    }

    // Test with zero (should fail as it's not positive)
    try {
        delay(0) { println("This should not execute") }
        println("❌ Zero test failed: accepted 0")
    } catch (e: IllegalArgumentException) {
        println("✅ Zero test passed: ${e.message}")
    }

    println("--- Number Validation Tests Complete ---\n")
}
