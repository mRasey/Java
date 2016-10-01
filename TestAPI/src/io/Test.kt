package io

fun main(args: Array<String>) {
    var t = Test()
    t.a = 1
    println(t.a)
    println(foo(b = 2))
}

fun foo(a: Int = 1, b: Int) = a + b

data class data (
        var a: Int,
        var b: Int
)

class Test {
    var a: Int = 0
        get() = field
        set(value) {
            field = value + 233
        }
    var b: Int = 0
        get() = field
        set(value) {
            field = value
        }
}
