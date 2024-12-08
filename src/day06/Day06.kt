package day06

import utils.readInput
import utils.println

typealias Pos = Pair<Int, Int>
typealias Dir = Pair<Int, Int>

fun Pos.move(dir: Dir) = Pos(first + dir.first, second + dir.second)

fun Dir.turnRight() = when(this) {
    0 to 1 -> 1 to 0
    1 to 0 -> 0 to -1
    0 to -1 -> -1 to 0
    else -> 0 to 1
}

data class Board(val rows: List<List<Char>>) {
    fun obstacle(at: Pos) = rows[at.first][at.second] == '#'
    fun inB(at: Pos) = at.first in rows.indices && at.second in rows[at.first].indices
    fun copyWithObsticle(at: Pos) = Board(rows.mapIndexed { i, r ->
        r.mapIndexed { j, c ->
            if(i == at.first && j == at.second) '#' else c
        }
    })
}

fun parse(input: List<String>): Triple<Board, Pos,Dir> {
    fun start(l: String, r: Int): Pair<Pos, Dir>? {
        l.indexOfFirst { it in "><^v" }.let { i ->
            if(i == -1) return null
            val dir = when {
                l.contains("v") -> 1 to 0
                l.contains(">") -> 0 to 1
                l.contains("<") -> 0 to -1
                else -> -1 to 0
            }
            return (r to i) to dir
        }
    }

    val start = input.mapIndexed{ i, l -> start(l, i) }.filterNotNull().first()

    val board = input.map { it.toList().map { if (it == '#') '#' else '.'  } }
    return Triple(Board(board), start.first, start.second)
}

fun Board.traverse(start: Pair<Pos,Dir>): Set<Pair<Pos, Dir>>? {
    val moves = mutableSetOf<Pair<Pos,Dir>>()
    moves.add(start)
    var curP = start.first
    var curD = start.second
    while(true) {
        val n = curP.move(curD)
        if(moves.contains(n to curD)) {
            return null
        }
        if(!inB(n)) {
            return moves
        }
        if(obstacle(n)) {
            curD = curD.turnRight()
        } else {
            curP = n
            moves.add(curP to curD)

        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val (board, start, dir) =  parse(input)
        return board.traverse(start to dir)!!.map { it.first }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val (board, start, dir) =  parse(input)

        val moves = board.traverse(start to dir)!!

        return moves.map { it.first }.distinct()
            .map { board.copyWithObsticle(it) }
            .count { it.traverse(start to dir) == null }
    }

    val trial = readInput("day06")
    val input = readInput("day06", "input")
    check(part1(trial) == 41)
    part1(input).println()

    part2(trial).println()
    check(part2(trial) == 6)
    part2(input).println()
}
