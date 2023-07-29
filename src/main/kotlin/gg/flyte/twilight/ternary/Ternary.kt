package gg.flyte.twilight.ternary

data class Ternary<T>(private val boolean: Boolean, private val `true`: T) {
    infix fun or(`false`: T): T = if (boolean) `true` else `false`
}

infix fun <T>Boolean.then(value: T): Ternary<T> = Ternary(this, value)