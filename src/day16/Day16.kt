package day16

import utils.println
import utils.readInput
import java.util.*
import kotlin.math.abs

typealias Map = Array<CharArray>

fun Map.find(x:Char): Point {
    for (i in this.indices) {
        for (j in this[i].indices) {
            if (this[i][j] == x) {
                return Point(i, j)
            }
        }
    }
    return Point(-1, -1)
}

fun Map.at(p: Point): Char {
    return this[p.first][p.second]
}

fun Map.idx(p: Point): Int {
    if(p.first < 0 || p.first >= this.size || p.second < 0 || p.second >= this[0].size) throw IndexOutOfBoundsException()
    return p.first * this[0].size + p.second
}

fun Map.size() = this.size * this[0].size


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

fun Point.distance(other: Point): Point {
    return abs(this.first - other.first) to abs(this.second - other.second)
}


fun parse(input: List<String>): Map {
    return input.map { it.toCharArray() }.toTypedArray()
}

data class Node(val id: Point, val distance: Int, val dir: Point): Comparable<Node> {
    override fun compareTo(other: Node): Int {
        return this.distance.compareTo(other.distance)
    }
}

fun Map.dijkstra(start: Point, end: Point): Pair<Int, Point> {
    val distances = IntArray(this.size()) { Int.MAX_VALUE }
    val priorityQueue = PriorityQueue<Node>()

    distances[this.idx(start)] = 0
    priorityQueue.add(Node(start, 0, turns[0]))

    while (priorityQueue.isNotEmpty()) {
        val currentNode = priorityQueue.poll()
        val currentDistance = currentNode.distance
        val currentId = currentNode.id

        if (currentId == end) {
            return currentDistance to currentNode.dir
        }

        if (currentDistance > distances[this.idx(currentId)]) continue

        val dir = currentNode.dir

        val next = sequenceOf(
            dir to 0,
            dir.turnLeft() to 1000,
            dir.turnRight() to 1000
        ).map { (d, c) ->
            val newPos = Point(currentId.first + d.first, currentId.second + d.second)
            Triple(newPos, d, c + 1)
        }.filter {
            this.at(it.first) != '#'
        }.toList()

        for (edge in next) {
            val newDistance = currentDistance + edge.third
            val idx = this.idx(edge.first)

            if (newDistance < distances[idx]) {
                distances[idx] = newDistance
                priorityQueue.add(Node(edge.first, newDistance, edge.second))
            }
        }
    }

    return Int.MAX_VALUE to turns[0]
}


fun main() {

//    fun part1(input: List<String>): Int {
//        var currentBest = Int.MAX_VALUE
//
//        val map = parse(input)
//
//        fun Map.dfs(pos: Point, visited: Set<Point>, dir: Point, cost: Int): Int {
//            if (this.at(pos) == 'E') {
//                if(cost < currentBest) currentBest = cost
//                return cost
//            }
//
//            val next  = sequenceOf(
//                dir to 0,
//                dir.turnLeft() to 1000,
//                dir.turnRight() to 1000
//            ).map { (d, c) ->
//                val newPos = Point(pos.first + d.first, pos.second + d.second)
//                Triple(newPos, d, c + 1)
//            }.filter {
//                this.at(it.first) != '#' && it.first !in visited
//            }.filter {
//                it.third + cost < currentBest
//            }.toList()
//
//            return next.minOfOrNull {
//                this.dfs(it.first, visited + it.first, it.second, cost + it.third)
//            } ?: Int.MAX_VALUE
//        }
//
//        map.dfs(map.find('S'), setOf(map.find('S')), turns[0], 0).let {
//            return it
//        }
//
//
//    }

    fun part1(input: List<String>): Int {
        val map = parse(input)
        return map.dijkstra(map.find('S'), map.find('E')).first
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)

        val start = map.find('S')
        val end = map.find('E')

        val(fuel, dir) = map.dijkstra(start, end)

        val allPaths = mutableListOf<List<Point>>()
        val currentPath = mutableListOf<Point>()

        fun Map.dfs(currentNode: Point, currentDir: Point, fuelLeft: Int) {
            currentPath.add(currentNode)

            if((fuelLeft == 0 || fuelLeft == 1000) && currentNode == start) {
                allPaths.add(currentPath.toList())
            } else {
                val next  = sequenceOf(
                    currentDir to 0,
                    currentDir.turnLeft() to 1000,
                    currentDir.turnRight() to 1000
                ).map { (d, c) ->
                    val newPos = Point(currentNode.first + d.first, currentNode.second + d.second)
                    Triple(newPos, d, c + 1)
                }.filter {
                    this.at(it.first) != '#'
                }.toList()

                for (edge in next) {
                    val newFuel = fuelLeft - edge.third

                    val dist = edge.first.distance(start)
                    val est = dist.toList().sum() + if(dist.toList().any { it == 0 }) 0 else 1000

                    if(newFuel >= est) {
                        this.dfs(edge.first, edge.second, newFuel)
                    }
                }
            }
            currentPath.removeAt(currentPath.size - 1)
        }

        map.dfs(end, dir.turnRight().turnRight(), fuel)

        return allPaths.flatten().distinct().size
    }

    val trial = readInput("day16")
//    val test = readInput("day16","test")
    val input = readInput("day16", "input")
//    part1(test).println()

    check(part1(trial) == 7036)
    part1(input).println()

    check(part2(trial) == 45)
    part2(input).println()

}
