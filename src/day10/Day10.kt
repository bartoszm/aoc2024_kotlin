package day10

import utils.readInput
import utils.println

fun List<List<Int>>.at(i: Int, j: Int): Int {
    if (i !in this.indices || j !in this[i].indices) return -1
    return this[i][j]
}

fun main() {

    fun parse(input: List<String>): List<List<Int>> {
        return input.map { it.map { l -> l.toString().toInt() } }
    }


    fun part1(input: List<String>): Long {
        val data = parse(input)

        fun calculate(current: Int, x: Int, y: Int): Int {
            if (current == 9) return 1

            return sequenceOf(
                x + 1 to y,
                x to y + 1,
                x - 1 to y,
                x to y - 1
            ).map { (a,b) -> data.at(a,b) to (a to b) }
            .filter { (v, _) -> v == current + 1 }
            .map { (_, p) -> calculate(current + 1, p.first, p.second) }
            .sum()

        }
        val res = sequence {
            for (i in data.indices) {
                for (j in data[i].indices) {
                    if (data[i][j] == 0) {
                        yield(calculate(0, i, j) to (i to j))
                    }
                }
            }
        }.toList()

        return res.map{(x, _) -> x}.sum().toLong()
    }

    fun part2(input: List<String>): Long {

        return 0L
    }

    val trial = readInput("day10")
    val input = readInput("day10", "input")

    part1(trial).println()

    check(part1(trial) == 36L)
    part1(input).println()

    check(part2(trial) == 2858L)
    part2(input).println()
}
