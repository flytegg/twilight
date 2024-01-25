package gg.flyte.twilight

import gg.flyte.twilight.extension.toDocument

data class Person(
    val name: String,
    val age: Int
)

fun main() {
    val person = Person("Akkih", 15)
    println(person.toDocument())
}