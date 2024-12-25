package day16

import utils.println
import utils.readInput
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs

typealias Map2D = Array<CharArray>

fun Map2D.find(x:Char): Point {
    for (i in this.indices) {
        for (j in this[i].indices) {
            if (this[i][j] == x) {
                return Point(i, j)
            }
        }
    }
    return Point(-1, -1)
}

fun Map2D.at(p: Point): Char {
    return this[p.first][p.second]
}

fun Map2D.idx(p: Point): Int {
    if(p.first < 0 || p.first >= this.size || p.second < 0 || p.second >= this[0].size) throw IndexOutOfBoundsException()
    return p.first * this[0].size + p.second
}

fun Map2D.size() = this.size * this[0].size


typealias Point = Pair<Int, Int>

val turns = listOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))

fun Point.turnLeft():Point {
    val idx = turns.indexOf(this)
    return turns[(idx + 1) % 4]
}

fun Point.turnRight():Point {
    val idx = turns.indexOf(this)
    return turns[(idx + 3) % 4]
}


fun parse(input: List<String>): Map2D {
    return input.map { it.toCharArray() }.toTypedArray()
}

data class Node(val id: Point, val distance: Int, val dir: Point): Comparable<Node> {
    override fun compareTo(other: Node): Int {
        return this.distance.compareTo(other.distance)
    }
}

data class Result(val distance: Int, val dir: Point, val predecessors: Map<Point, Set<Point>>)

fun Map2D.forward(n: Node): Node? {
    val dir = n.dir
    val newPos = Point(n.id.first + dir.first, n.id.second + dir.second)
    return if (this.at(newPos) != '#') Node(newPos, n.distance + 1, dir) else null
}

fun left(n: Node) = Node(n.id, n.distance + 1000, n.dir.turnLeft())
fun right(n: Node) = Node(n.id, n.distance + 1000, n.dir.turnRight())

fun Map2D.traverse(): Pair<Int, MutableMap<Node, MutableSet<Node>>> {
    val start = this.find('S')
    val end = this.find('E')
    val predecessors = mutableMapOf<Node, MutableSet<Node>>()

    val queue = PriorityQueue<Node>()
    queue.add(Node(start, 0, turns[0]))
    val visited = mutableSetOf<Pair<Point, Point>>()

    while(queue.isNotEmpty()) {
        val current = queue.poll()
        if(current.id == end) return current.distance to predecessors
        if(visited.add(current.id to current.dir)) {
            sequenceOf(
                this.forward(current),
                left(current),
                right(current)
            ).filterNotNull().forEach {

                predecessors.computeIfAbsent(it){ mutableSetOf()}.add(current)
                queue.add(it)
            }
        }
    }
    return -1 to predecessors
}


fun main() {
    fun part1(input: List<String>): Int {
        val map = parse(input)
        return map.traverse().first
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)
        val endPos = map.find('E')
        val (_, predecessors) = map.traverse()
        val end = predecessors.keys.first{ it.id == endPos }

        val queue = ArrayDeque<Node>()
        queue.add(end)
        return sequence {
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                yield(current.id)
                val pred = predecessors[current] ?: emptySet()
                queue.addAll(pred)
            }
        }.toSet().size
    }

    val trial = readInput("day16")
    val input = readInput("day16", "input")

    check(part1(trial) == 7036)
    part1(input).println()

    check(part2(trial) == 45)
    part2(input).println()

}
