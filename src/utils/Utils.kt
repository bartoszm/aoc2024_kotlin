package utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, file:String = "trial") = Path("data/$name/$file.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun <T> List<T>.toPair(): Pair<T, T> {
    require(this.size == 2) { "List $this is not of length 2!" }
    val (a, b) = this
    return Pair(a, b)
}

fun <T> List<String>.convert(converter: (String) -> List<T>) = this.map(converter)


fun <T> List<T>.combine(): List<Pair<T, T>> {
    return this.flatMapIndexed { i, a ->
        this.subList(i + 1, this.size).map { b -> a to b }
    }
}

class Memoize<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = mutableMapOf<T, R>()
    override fun invoke(v: T): R {
        return values.getOrPut(v) {
            val r = f(v)
            r
        }
    }
}