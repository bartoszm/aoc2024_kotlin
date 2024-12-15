package day11

import utils.println
import utils.readInput
import kotlin.system.measureTimeMillis


fun main() {

    fun parse(input: List<String>): List<Long> {
       return input[0].split(" ").map { it.toLong() }
    }

    fun String.splLong() = listOf(this.substring(0, this.length / 2).toLong(), this.substring(this.length / 2).toLong())

    fun List<Long>.freq() = this.groupingBy { it }.eachCount()

    fun blink(times: Int, d: List<Long>) : Int {
        var data = d
        fun convert(d: List<Long>) : List<Long> {
            return d.flatMap {
                val str = it.toString()
                when {
                    it == 0L -> listOf(1L)
                    str.length % 2 == 0 -> str.splLong()

                    else -> listOf(it * 2024)
                }
            }
        }

        for (i in 1..times) {
            data = convert(data)
        }
        return data.size
    }

    fun part1(input: List<String>): Int {
        return blink(25, parse(input))
    }

    fun step(v: Long): List<Long> {
        val str = v.toString()
        return when {
            v == 0L -> listOf(1L)
            str.length % 2 == 0 -> str.splLong()
            else -> listOf(v * 2024)
        }
    }

    fun buildCache(): Map<Long, List<Long>> {
        val cache = mutableMapOf<Long, List<Long>>()
        for (i in 1..9) {
            cache[i.toLong()] = step(i.toLong())
        }

        do {
            val generated = cache.values.flatMap { v ->
                v
                    .filter { x -> !cache.containsKey(x) }
                    .map { x -> x to step(x) }
            }
            generated.forEach { (k, v) -> cache[k] = v }
        } while (generated.isNotEmpty())
        return cache
    }

    val cache = buildCache()

    fun collect(v:Long, steps: Int): List<Long>  {
        if(steps == 1) return cache[v] ?: step(v)
        return (cache[v] ?: step(v)).flatMap { collect(it, steps - 1) }

    }

    fun part2(input: List<String>): Long {
        val starting = parse(input)

        val frequencies = mutableMapOf<Long, Map<Long, Int>>()

        for(i in 0..9L) {
            frequencies[i] = collect(i, 25).freq()
        }

        for (i in starting) {
            if (!frequencies.containsKey(i)) {
                frequencies[i] = collect(i, 25).freq()
            }
        }

        for (i in 0..1) {
            val toCalculate = frequencies.values.flatMap { it.keys }.toSet().filter { !frequencies.containsKey(it) }
            for (j in toCalculate) {
                frequencies[j] = collect(j, 25).freq()
            }
        }

        fun calculate(v: Long, step: Int) : Long {
            if(step == 0) return frequencies[v]!!.values.sum().toLong()
            return frequencies[v]!!.map { (k, v) -> calculate(k, step - 1) * v }.sum()
        }

        return starting.sumOf { calculate(it, 2) }

    }

    val trial = readInput("day11")
    val input = readInput("day11", "input")

    fun part1a(input: List<String>): Int {
        val data = parse(input)

        return data
            .map { collect(it,25).freq() }
            .sumOf { it.values.sum() }
    }

    val t2 = measureTimeMillis {
        check(part1a(trial) == 55312)
    }
    println("Time: $t2 ms")

    val t = measureTimeMillis {
        check(part1(trial) == 55312)
    }
    println("Time: $t ms")

    part1a(input).println()
    part2(input).println()
}
