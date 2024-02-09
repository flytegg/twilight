package gg.flyte.twilight

fun main() {
    testVersionSubstring()
}

private fun testVersionSubstring() {
    listOf(
        "org.bukkit.craftbukkit.v1_20_R1",
        "org.bukkit.craftbukkit.v1_20_R2",
        "org.bukkit.craftbukkit.v1_20_R3"
    ).forEach {
        println(it.run { substring(lastIndexOf('.') + 1) })
    }
}