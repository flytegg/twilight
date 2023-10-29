package gg.flyte.twilight

import gg.flyte.twilight.extension.applyForEach

fun main() {
    val list = listOf("test", "asd", "34534534asd", "asdas2sd")
    list.applyForEach {
        println(this)
    }
}