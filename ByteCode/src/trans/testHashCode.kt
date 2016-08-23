package trans

import java.util.*

/**
 * Created by Billy on 2016/8/15.
 */
fun main(args: Array<String>) {
    var array = ArrayList<Integer>()
    array.add(Integer(1))
    array.add(Integer(2))
    array.add(1, Integer(100))
    for(i in array)
        println(i)
}

enum class E{
    A,
    B
}