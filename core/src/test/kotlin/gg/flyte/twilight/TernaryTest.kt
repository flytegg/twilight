package gg.flyte.twilight

import gg.flyte.twilight.ternary.then

fun main() {
    ternaryTest()
}

fun ternaryTest() {
    val test = false
    println(test then "yes" or "no")

    test then success() or fail()
}

private fun success() {
    println("YAAAA")
}

private fun fail() {
    println("ShIIITI")
}