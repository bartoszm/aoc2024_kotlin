package day12

import utils.println
import utils.readInput

typealias Point = Pair<Int, Int>

fun parse(input: List<String>): Array<CharArray> {
    return input.map { it.toCharArray() }.toTypedArray()
}

class Region(val c: Char) {
    val coordinates = mutableSetOf<Point>()

    val size: Int
        get() = coordinates.size


    override fun toString(): String {
        return "Region(c=$c, coordinates=${coordinates.sortedBy { it.first * 100 + it.second }})"
    }


}

fun findReqions(data: Array<CharArray>): List<Region> {
    val regions = mutableListOf<Region>()
    val boolArray: Array<BooleanArray> = Array(data.size) { BooleanArray(data[it].size) { false } }


    fun dfs(x: Int, y: Int, region: Region) {
        if(x !in data.indices || y !in data[x].indices) return
        if (boolArray[x][y] || data[x][y] != region.c) return
        boolArray[x][y] = true
        region.coordinates.add(x to y)
        dfs(x + 1, y, region)
        dfs(x - 1, y, region)
        dfs(x, y + 1, region)
        dfs(x, y - 1, region)
    }

    for (i in data.indices) {
        for (j in data[i].indices) {
            if (!boolArray[i][j]) {
                val region = Region(data[i][j])
                dfs(i, j, region)
                regions.add(region)
            }
        }
    }

    return regions
}


fun main() {

    fun calculate(input: List<String>, perimeter: (Region)-> Int): Int {
        val data = parse(input)
        val regions = findReqions(data)
        return regions.sumOf { it.size * perimeter(it) }
    }

    fun part1(input: List<String>): Int {
        fun Region.perimeter()= coordinates.sumOf { (x, y) ->
            sequenceOf(
                x + 1 to y,
                x to y + 1,
                x - 1 to y,
                x to y - 1
            ).count { (a, b) -> ! coordinates.contains(a to b) }
        }

        return calculate(input) { it.perimeter() }
    }





    fun part2(input: List<String>): Int {

        fun Region.bounds(): Pair<Point, Point> {
            val x = coordinates.map { it.first }
            val y = coordinates.map { it.second }
            return (x.min() to y.min()) to (x.max() to y.max())
        }

        fun Region.inBounds(p: Point): Boolean {
            val (tl, br) = this.bounds()
            return p.first in tl.first..br.first && p.second in tl.second..br.second
        }


        fun Region.collect(start: Point, next: (Point)-> Point, prev: (Point) -> Point): List<Point> {
            var curr = start
            return sequence {
                while(inBounds(curr)) {
                    if(coordinates.contains(curr) && !coordinates.contains(prev(curr))) {
                        yield(curr)
                    }
                    curr = next(curr)
                }
            }.toList()

        }

        fun List<Int>.countConsecutive(): Int {
            if(this.size == 1) return 1
            return this.sorted().windowed(2)
                .map { it[1] - it[0] }
                .count { it > 1 } + 1
        }

        fun List<Point>.countEdges(): Int {
            return this.groupBy({ it.second }, { it.first }).values.sumOf { it.countConsecutive() }
        }

        fun Region.perimeter(): Int {

            val (tl, br) = this.bounds()

            val ledge = (tl.first..br.first).flatMap {
                this.collect(it to tl.second, { (x, y) -> x to y + 1 }, { (x, y) -> x to y - 1 })
            }

            val redge = (tl.first..br.first).flatMap {
                this.collect(it to br.second, { (x, y) -> x to y - 1 }, { (x, y) -> x to y + 1 })
            }

            val tedge = (tl.second..br.second).flatMap {
                this.collect(tl.first to it, { (x, y) -> x + 1 to y }, { (x, y) -> x - 1 to y })
                    .map { p -> p.second to p.first }
            }

            val bedge = (tl.second..br.second).flatMap {
                this.collect(br.first to it, { (x, y) -> x - 1 to y }, { (x, y) -> x + 1 to y })
                    .map { p -> p.second to p.first }
            }

            return listOf(ledge, redge, tedge, bedge).sumOf { it.countEdges() }

        }

        return calculate(input) { it.perimeter() }
    }


    val trial = readInput("day12")
    val input = readInput("day12", "input")

    check(part1(trial) == 1930)
    part1(input).println()

    check(part2(trial) == 1206)
    part2(input).println()

}
