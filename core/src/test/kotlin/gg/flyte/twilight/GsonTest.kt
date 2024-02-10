package gg.flyte.twilight

import gg.flyte.twilight.gson.Exclude
import gg.flyte.twilight.gson.toJson

class GsonTest(
    val include: String = "included",
    @Exclude val exclude: String = "excluded"
)

fun main() {
    val json = GsonTest().toJson()
    println(json)
}