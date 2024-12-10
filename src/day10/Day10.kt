package day10

import utils.readInput
import utils.println

fun List<List<Int>>.at(i: Int, j: Int): Int {
    if (i !in this.indices || j !in this[i].indices) return -1
    return this[i][j]
}

fun List<List<Int>>.next(value: Int, x: Int, y: Int) = sequenceOf(
    x + 1 to y,
    x to y + 1,
    x - 1 to y,
    x to y - 1
).map { (a,b) -> this.at(a,b) to (a to b) }
.filter { (v, _) -> v == value + 1 }

fun main() {

    fun parse(input: List<String>): List<List<Int>> {
        return input.map { it.map { l -> l.toString().toInt() } }
    }

    fun summarize(data: List<List<Int>>, calculate: (a: Int, b: Int, c:Int) -> Int ): Long {
        return sequence {
            for (i in data.indices) {
                for (j in data[i].indices) {
                    if (data[i][j] == 0) {
                        yield(calculate(0, i, j))
                    }
                }
            }
        }.sum().toLong()
    }

    fun part1(input: List<String>): Long {
        val data = parse(input)

        fun calculate(current: Int, x: Int, y: Int): Sequence<Pair<Int, Int>> {
            if (current == 9) return sequenceOf(x to y)
            return data.next(current, x,y)
            .flatMap { (_, p) -> calculate(current + 1, p.first, p.second) }
            .distinct()
        }
        return summarize(data) { a, b, c -> calculate(a, b, c).count() }
    }

    fun part2(input: List<String>): Long {
        val data = parse(input)

        fun calculate(current: Int, x: Int, y: Int): Int {
            if (current == 9) return 1
            return data.next(current, x,y)
                .map { (_, p) -> calculate(current + 1, p.first, p.second) }
                .sum()
        }

        return summarize(data) { a, b, c -> calculate(a, b, c) }
    }

    val trial = readInput("day10")
    val input = readInput("day10", "input")

    check(part1(trial) == 36L)
    part1(input).println()

    check(part2(trial) == 81L)
    part2(input).println()
}
