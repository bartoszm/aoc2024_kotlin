package day07

import utils.readInput
import utils.println
import utils.toPair

typealias Row = Pair<Long, List<Long>>

fun main() {
    fun parse(input: List<String>): List<Row> {
        fun parse(l: String): Row {
            l.split(":").toPair().let { (a, b) ->
                return a.toLong() to b.split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.trim().toLong() }
            }
        }

        return input.map(::parse)
    }

    fun classify1(input: Row): Boolean {
        val goal = input.first
        val list = input.second

        fun generate(current: Long, index: Int): Boolean {
            if (current == goal) return true
            if (current > goal) return false
            if (index == list.size) return false

            return generate(current + list[index], index + 1) || generate(current * list[index], index + 1)
        }

        return generate(list[0], 1)
    }

    fun part1(input: List<String>): Long {
        val data = parse(input).filter { classify1(it) }
        return data.sumOf { v -> v.first }

    }

    fun classify2(input: Row): Boolean {
        val goal = input.first
        val list = input.second

        fun glue(a:Long, b: Long) = "$a$b".toLong()

        fun generate(current: Long, index: Int): Boolean {
            if (current > goal) return false
            if(index == list.size) return current == goal

            return sequence {
                yield(generate(current + list[index], index + 1))
                yield(generate(current * list[index], index + 1))
                yield(generate(glue(current, list[index]), index + 1))
            }.any {it}
        }

        return generate(list[0], 1)
    }

    fun part2(input: List<String>): Long {
        val data = parse(input).filter { classify2(it) }
        return data.sumOf { v -> v.first }
    }

    val trial = readInput("day07")
    val input = readInput("day07", "input")
    check(part1(trial) == 3749L)
    part1(input).println()

    check(part2(trial) == 11387L)
    //169122157276284
    //169122112716571
    part2(input).println()
}
