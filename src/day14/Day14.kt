package day14

import utils.println
import utils.readInput

typealias Point = Pair<Int, Int>


fun Int.addMod(p: Int, mod: Int): Int {
    return (((this + p) % mod) + mod) % mod
}

data class Robot(val position: Point, val vel: Point) {
    fun move(times: Int = 1, bounds: Point): Robot {
        val x = position.first.addMod(vel.first * times, bounds.first)
        val y = position.second.addMod(vel.second * times, bounds.second)
        return copy(position = x to y)
    }
}



fun parse(input: List<String>): List<Robot> {

    val r = """-?\d+""".toRegex()
    return input.map { v ->
        val l = r.findAll(v).map { it.value.toInt() }.toList()
        Robot(l[0] to l[1], l[2] to l[3])
    }
}


fun main() {

    fun part1(input: List<String>, bounds: Point): Int {
        val robots = parse(input)

        val after100 = robots.map { r -> r.move(100, bounds) }.sortedBy { it.position.first * 100 + it.position.second }

        val countLT = after100.filter { r -> r.position.first < bounds.first / 2 && r.position.second < bounds.second / 2 }.size
        val countRT = after100.filter { r -> r.position.first > bounds.first / 2 && r.position.second < bounds.second / 2 }.size
        val countLB = after100.filter { r -> r.position.first < bounds.first / 2 && r.position.second > bounds.second / 2 }.size
        val countRB = after100.filter { r -> r.position.first > bounds.first / 2 && r.position.second > bounds.second / 2 }.size
        return countLT * countRT * countLB * countRB
    }

    fun List<Robot>.align() = this.groupBy{it.position.second}.mapValues { it.value.sortedBy { it.position.first } }

    fun allConsecutive(l: List<Int>) = l.windowed(2).all { it[1] - it[0] == 1 }


    fun hasRobotsInRow(robots: Map<Int,List<Robot>>, c: Int = 3): Boolean {
        val roboRows = robots.values
        return roboRows
            .map { it.map { r -> r.position.first }.distinct() }
            .filter { it.size >= c }
            .map { it.windowed(c).any { w -> allConsecutive(w) } }
            .firstOrNull() ?: false

    }

    fun print(robots: Map<Int, List<Robot>>, size: Point) {
        for(y in 0 until size.second) {
            val row = robots[y] ?: emptyList()
            for(x in 0 until size.first) {
                if(row.any { it.position.first == x }) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun part2(input: List<String>, bounds: Point): Long {
        val robots = parse(input)
        for(s in 1..10000) {
            val next = robots.map { r -> r.move(s, bounds) }
            val aligned = next.align()
            if(hasRobotsInRow(aligned,7)) {
                println("Step: ${s}")
                print(aligned, bounds)

            }
        }
        return 7093L
    }

    val trial = readInput("day14")
    val input = readInput("day14", "input")

    check(part1(trial, 11 to 7) == 12)
    part1(input, 101 to 103).println()

    part2(input, 101 to 103).println()

}
