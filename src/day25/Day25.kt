package day25

import utils.println
import utils.readInput

sealed class Shape(open val rows: List<Int>, open val size: Int) {
    fun fits(s: Shape) = this.rows.zip(s.rows).all { (a, b) -> a+b < size }
}
data class Lock(override val rows: List<Int>, override val size: Int): Shape(rows, size)
data class Key(override val rows: List<Int>, override val size: Int): Shape(rows, size)

fun parse(input: List<String>): List<Shape> {
    fun add(l: String, acc:List<Int>) = l.map { if(it == '#') 1 else 0 }.zip(acc).map { (a, b) -> a + b }

    return sequence {
        var buffer = mutableListOf<String>()
        for (line in input) {
            if (line.isEmpty()) {
                yield(buffer)
                buffer = mutableListOf()
            } else {
                buffer.add(line)
            }
        }
        yield(buffer)
    }.map { shape ->
        if(shape[0].all { it == '#' }) {
            Lock(shape.drop(1).fold(List(shape[0].length){0}) { acc, s -> add(s, acc) }, shape.size - 1)
        } else {
            Key(shape.dropLast(1).fold(List(shape[0].length){0}) { acc, s -> add(s, acc) }, shape.size - 1)
        }
    }.toList()
}

fun part1(input: List<String>): Int {
    val shapes = parse(input)

    val locks = shapes.filterIsInstance<Lock>()
    val keys = shapes.filterIsInstance<Key>()

    return sequence {
        for(l in locks) {
            for(k in keys) {
                if (l.fits(k)) {
                    yield(l to k)
                }
            }
        }
    }.count()
}

fun part2(input: List<String>): Long {
    TODO()
}

fun main() {
    val trial = readInput("day25")
    val input = readInput("day25", "input")
    check(part1(trial) == 3)
    part1(input).println()
    part2(input).println()
}