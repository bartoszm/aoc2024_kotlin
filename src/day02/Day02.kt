package day02

import utils.readInput
import utils.println
import kotlin.math.absoluteValue
import kotlin.math.sign


fun main() {
    fun parse(str: String): List<Int> = str.split("""\s+""".toRegex()).map { it.toInt() }

    fun isSafe(a: Int, b: Int, dir: Boolean): Boolean {
        val dif = (a - b)
        val difA = (a - b).absoluteValue
        return (dir == dif > 0) && difA > 0 && difA <= 3
    }

    fun isSafe(report: List<Int>): Boolean {
        val dir = report[0] - report[1] > 0

        val result = report.windowed(2, 1).all { (a, b) ->
            isSafe(a, b, dir)
        }
        return result
    }

    fun part1(input: List<String>): Int {
        return input.map { parse(it) }
            .count { isSafe(it) }
    }

    fun part2(input: List<String>): Int {
        fun isSafe2(report: List<Int>): Boolean {
            val safe = isSafe(report)
            if (safe) return true
            return report.indices.map { index ->
                report.filterIndexed { i, _ -> i != index }
            }.any{isSafe(it)}
        }

        return input.map { parse(it) }
            .count { isSafe2(it) }

    }

    val trial = readInput("day02")
    val input = readInput("day02", "input")
    check(part1(trial) == 2)
    part1(input).println()

    check(part2(trial) == 4)
    part2(input).println()
}
