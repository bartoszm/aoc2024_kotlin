package day18

import utils.println
import utils.readInput
import utils.toPair
import java.util.*

typealias Point = Pair<Int, Int>

fun Point.idx(size: Int) = this.first  + this.second * size

data class Input(val size: Int, val points: List<Point>)

fun parse(input: List<String>): Input {
    val size = input[0].toInt()
    val points = input.drop(2).map {
        it.split(",").map { it.toInt() }.toPair()
    }
    return Input(size, points)
}

val turns = listOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))


data class Node(val vertex: Point, val dist: Int) : Comparable<Node> {
    override fun compareTo(other: Node) = dist.compareTo(other.dist)
}

fun dijkstra(start: Point, end: Point, size: Int, corrupted: Set<Point>): Int {
    val dist = IntArray(size * size) { Int.MAX_VALUE }
    dist[start.idx(size)] = 0
    val pq = PriorityQueue<Node>()
    pq.add(Node(start, 0))

    while (pq.isNotEmpty()) {
        val (u, d) = pq.poll()
        if (d > dist[u.idx(size)]) continue

        if (u == end) {
            return d
        }

        turns.map { Point(u.first + it.first, u.second + it.second) }
            .filter{ it.first in 0 until size && it.second in 0 until size }
            .filter { it !in corrupted }
            .forEach { v ->
                val nd = d + 1
                if (nd < dist[v.idx(size)]) {
                    dist[v.idx(size)] = nd
                    pq.add(Node(v, nd))
                }
            }
    }
    return -1
}


fun main() {

    fun part1(data: List<String>, toConsider: Int): Int {
        val input = parse(data)

        val corrupted = input.points.take(toConsider).toSet()

        val end = input.size - 1 to input.size - 1


        return dijkstra(0 to 0, end, input.size, corrupted)
    }

    fun part2(input: List<String>): Point {
        val data = parse(input)
        for (x in 12 .. data.points.size) {
            val corrupted = data.points.take(x).toSet()
            val end = data.size - 1 to data.size - 1
            val res = dijkstra(0 to 0, end, data.size, corrupted)
            if (res == -1) {
                return data.points[x-1]
            }
        }
        return -1 to -1
    }

    val trial = readInput("day18")
    val input = readInput("day18", "input")



    check(part1(trial, 12) == 22)
    part1(input, 1024).println()
    check(part2(trial) == 6 to 1)
    part2(input).println()

}
