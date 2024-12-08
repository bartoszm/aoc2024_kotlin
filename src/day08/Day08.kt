package day08

import utils.readInput
import utils.println

typealias Point = Pair<Int, Int>

data class Position(val p: Point, val v: Char)


operator fun Point.minus(p: Point) = Point(first - p.first, second - p.second)
operator fun Point.plus(p: Point) = Point(first + p.first, second + p.second)

fun main() {
    fun parse(input: List<String>): List<Position> {
        fun parse(r: Int, l: String): Sequence<Position> {
            return l.asSequence()
                .mapIndexed { i, c -> Position(r to i, c) }
                .filter { it.v.isLetterOrDigit() }
        }
        return input.flatMapIndexed { i, l -> parse(i, l).toList() }
    }

    fun List<Point>.combine(): List<Pair<Point, Point>> {
        return sequence {
            val l = this@combine
            for (i in l.indices) {
                for (j in i + 1 until l.size) {
                    yield(l[i] to l[j])
                }
            }
        }.toList()
    }

    fun List<String>.inB(at: Point) = at.first in indices && at.second in this[0].indices
    fun distance(a: Point, b: Point) = (b.first - a.first) to (b.second - a.second)

    data class Context(val a: Point, val b: Point, val distance: Point)

    fun process(input: List<String>, generator: (Context) -> Sequence<Point>): Int {
        val data = parse(input).groupBy { it.v }

        val antiAntennas = data.values.flatMap { v ->
            v.map { it.p }.combine()
                .flatMap { (a, b) -> generator(Context(a, b, distance(a, b))) }
        }.distinct()
        return antiAntennas.count()
    }

    fun part1(input: List<String>) = process(input) { (a, b, d) ->
        sequenceOf(a - d, b + d).filter { k -> input.inB(k) }
    }

    fun part2(input: List<String>) = process(input) { (a, b, d) ->
        generateSequence(a) { acc -> acc - d }.takeWhile { input.inB(it) } +
                generateSequence(b) { acc -> acc + d }.takeWhile { input.inB(it) }
    }

    val tiny = readInput("day08", "tiny")
    val trial = readInput("day08")
    val input = readInput("day08", "input")
    part1(tiny).println()
    check(part1(trial) == 14)
    part1(input).println()

    check(part2(trial) == 34)
    part2(input).println()
}
