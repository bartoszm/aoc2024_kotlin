package day12

import utils.println
import utils.readInput


fun parse(input: List<String>): Array<CharArray> {
    return input.map { it.toCharArray() }.toTypedArray()
}

class Region(val c: Char) {
    val coordinates = mutableSetOf<Pair<Int, Int>>()

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
        fun Region.perimeter(): Int {
            return 1
        }

        return calculate(input) { it.perimeter() }
    }


    val trial = readInput("day12")
    val input = readInput("day12", "input")

    check(part1(trial) == 1930)
    part1(input).println()

    check(part2(trial) == 81)
    part2(input).println()

}
