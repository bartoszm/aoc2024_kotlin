package day04

import utils.readInput
import utils.println

typealias Point = Pair<Int, Int>

fun Point.add(other: Point): Point {
    return this.first + other.first to this.second + other.second
}

data class Letter(val c: Char, val coordinates: Point)


data class Board(val dim: Pair<Int, Int> , val letters: Set<Letter>) {
    fun findByChar(c: Char): List<Letter> {
        return letters.filter { it.c == c }
    }
}

fun Board.word(w: String, start: Point, dir: Point): List<Letter>? {

    val word = sequence {
        var point = start
        for(c in w) {
            yield(Letter(c, point))
            point = point.add(dir)
        }
    }.toList()

    if (word.all { it in this.letters }) {
        return word
    }
    return null
}

fun main() {
    fun parse(lines: List<String>): Board {
        val dim = lines.size to lines[0].length
        val letters = mutableListOf<Letter>()
        for (i in lines.indices) {
            for (j in lines[i].indices) {
                letters.add(Letter(lines[i][j], i to j))
            }
        }
        return Board(dim, letters.toSet())
    }

    fun part1(input: List<String>): Int {
        val board = parse(input)


        val dirs = listOf(
            0 to 1,
            0 to -1,
            1 to 0,
            -1 to 0,
            1 to 1,
            1 to -1,
            -1 to 1,
            -1 to -1
        )

        fun allWords(w: String, start: Point): List<List<Letter>> {
            return dirs.mapNotNull { board.word(w, start, it) }
        }

        val words = board.findByChar('X').flatMap {l -> allWords("XMAS", l.coordinates).map { l to it } }

        return words.count()
    }

    fun part2(input: List<String>): Int {
        val board = parse(input)


        val dirs = listOf(
            1 to 1,
            1 to -1,
            -1 to 1,
            -1 to -1
        )
        fun allWords(w: String, start: Point): List<List<Letter>> {
            return dirs.mapNotNull { board.word(w, start, it) }
        }

        val words = board.findByChar('M').flatMap {l -> allWords("MAS", l.coordinates) }
        val xes = words.groupBy { it[1] }.filter { it.value.size == 2 }

        return xes.size

    }

    val trial = readInput("day04")
    val input = readInput("day04", "input")
    check(part1(trial) == 18)
    part1(input).println()

    check(part2(trial) == 9)
    part2(input).println()
}
