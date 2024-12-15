package day15

import utils.println
import utils.readInput


typealias Map = Array<CharArray>
typealias Point = Pair<Int, Int>
data class Move(val name: Char, val dir: Point)

fun Point.add(p: Point) = first + p.first to second + p.second
fun Point.sub(p: Point) = first - p.first to second - p.second

fun Map.findRobot(): Point {
    for (y in indices) {
        for (x in this[y].indices) {
            if (this[y][x] == '@') {
                return x to y
            }
        }
    }
    error("Robot not found")
}

fun Map.move(from: Point, to: Point) {
    this[to.second][to.first] = this[from.second][from.first]
    this[from.second][from.first] = '.'
}

fun Map.at(p: Point) = this[p.second][p.first]

fun Map.posOf(c: Char) = sequence {
    for (y in this@posOf.indices) {
        for (x in this@posOf[y].indices) {
            if (this@posOf[y][x] == c) {
                yield(x to y)
            }
        }
    }
}.toList()

fun parse(input: List<String>): Pair<Map, List<Move>> {
    fun toMove(c: Char) = when(c) {
        '^' -> 0 to -1
        'v' -> 0 to 1
        '<' -> -1 to 0
        '>' -> 1 to 0
        else -> error("Invalid direction")
    }

    val map = input.takeWhile { it.isNotEmpty() }.map { l ->
        l.toCharArray()
    }.toTypedArray()

    val moves = input.dropWhile { it.isNotEmpty() }.drop(1).flatMap { l ->
        l.toCharArray().map { Move(it, toMove(it)) }
    }
    return map to moves
}


fun main() {

    fun part1(input: List<String>): Int {
        val (map, moves) = parse(input)

        fun Map.moveStartsFrom(start: Point, dir:Point): Point? {
            var curr = start
            do {
                val next = curr.add(dir)
                if (this.at(next) == '#') {
                    return null
                }
                if (this.at(next) == '.') {
                    return curr
                }
                curr = next
            } while(true)
        }

        fun generateMoves(to: Point, from: Point, dir: Point) =  sequence {
            var curr = to
            while(curr != from) {
                yield(curr)
                curr = curr.sub(dir)
            }
            yield(from)
        }

        var pos = map.findRobot()
        for(m in moves) {
            map.moveStartsFrom(pos, m.dir)?.let {
                generateMoves(it, pos, m.dir).forEach { p ->
                    map.move(p, p.add(m.dir))
                }
                pos = pos.add(m.dir)
            }
        }

        return map.posOf('O').sumOf { it.first + it.second * 100 }
    }

    fun part2(input: List<String>): Int {
        fun Map.findShiftsHorizontal(curr: Point, m: Move): List<Point> {
            val shifts = mutableListOf<Point>()
            shifts.add(curr)
            var next = curr.add(m.dir)
            while(this.at(next)  !in setOf('#', '.')) {
                shifts.add(next)
                next = next.add(m.dir)
            }
            if(this.at(next) == '#') return emptyList()
            return shifts.reversed()
        }

        fun Map.pushesVertical(curr: Point, m: Move): List<Point>? {

            if(this.at(curr) == '#') return null
            if(this.at(curr) == '.') return listOf()
            if(this.at(curr) == '@') {
                val shifts = pushesVertical(curr.add(m.dir), m)
                return if(shifts == null) null else  shifts + listOf(curr)
            }

            val moves = if (this.at(curr) == ']') {
                listOf(curr, curr.add(-1 to 0))
            } else {
                listOf(curr, curr.add(1 to 0))
            }

            val shifts = moves.map { pushesVertical(it.add(m.dir), m) }
            if(shifts.any { it == null }) return null
            return shifts.flatMap { it!! } + moves
        }

        fun Map.findShiftsVertical(curr: Point, m: Move): List<Point> {
            val result = pushesVertical(curr, m) ?: return emptyList()
            return result.distinct().sortedBy { it.second * (-m.dir.second)  }
        }

        fun Map.resize() = this.map { row ->
            row.flatMap { when(it) {
                'O' -> listOf('[', ']')
                '@' -> listOf('@', '.')
                else -> listOf(it, it)
            } }.toCharArray()
        }.toTypedArray()

        val (map, moves) = parse(input).let { (m, ms) -> m.resize() to ms }

        fun Map.move(pos: Point, m: Move): Point {
            val shifts = if(m.dir.second == 0) {
                this.findShiftsHorizontal(pos, m)
            } else {
                this.findShiftsVertical(pos, m)
            }

            for(s in shifts) {
                this.move(s, s.add(m.dir))
            }

            return if(shifts.isEmpty()) pos else pos.add(m.dir)
        }

        var pos = map.findRobot()
        for(m in moves) {
            pos = map.move(pos, m)
        }

        return map.posOf('[').sumOf { it.first + it.second * 100 }
    }

    val trial = readInput("day15")
    val test = readInput("day15","test")
    val input = readInput("day15", "input")
    part1(test).println()

    check(part1(trial) == 10092)
    part1(input).println()

    check(part2(trial) == 9021)
    part2(input).println()

}
