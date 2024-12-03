package day03

import utils.readInput
import utils.println
import kotlin.math.absoluteValue


fun main() {
    val mul = Regex("""mul\((\d+),(\d+)\)""")

    fun multiply(line: String): Long {
        return mul.findAll(line).map {
            val (a, b) = it.destructured
            a.toLong() * b.toLong()
        }.sum()
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { multiply(it) }
    }

    fun part2(input: List<String>): Long {
        val full = input.joinToString { it }

        return full.split("do()").map {
            it.split("don't()")[0]
        }.sumOf { multiply(it) }

    }

    val trial = readInput("day03")
    val trial2 = readInput("day03", "trial2")
    val input = readInput("day03", "input")
    check(part1(trial) == 161L)
    part1(input).println()
    println(input.size)
    check(part2(trial2) == 48L)
    part2(input).println()
}
