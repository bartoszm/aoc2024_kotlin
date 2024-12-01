package day01

import utils.readInput
import utils.println
import kotlin.math.absoluteValue


fun main() {
    fun parse(str: String): Pair<Int, Int> = str.split("""\s+""".toRegex()).let { (a, b) -> a.toInt() to b.toInt() }

    fun part1(input: List<String>): Long {
        val (first,second) = input.map { parse(it) }.unzip()
        return first.sorted().zip(second.sorted())
            .sumOf { (a, b) -> (a.toLong() - b.toLong()).absoluteValue }
    }

    fun part2(input: List<String>): Long {
        val (first,second) = input.map { parse(it) }.unzip()
        val freq = second.groupingBy { it }.eachCount()
        return first.sumOf { freq.getOrDefault(it, 0) * it.toLong() }
    }

    val trial = readInput("day01")
    val input = readInput("day01", "input")
    check(part1(trial) == 11L)
    part1(input).println()

    check(part2(trial) == 31L)
    part2(input).println()
}
