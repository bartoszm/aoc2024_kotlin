package day13

import utils.println
import utils.readInput

data class Button(val x: Long, val y: Long, val cost: Int)
data class Goal(val x: Long, val y: Long)

fun main() {
    fun parse(data: List<String>): List<Triple<Button, Button, Goal>> {
        var a: Button? = null
        var b: Button? = null
        var g: Goal
        return sequence {
            for(l in data.indices) {
                if (data[l].startsWith("Button A")) {
                    val (x, y) = data[l].split(":")[1].split(",").map { it.split("+")[1].toLong() }
                    a = Button(x, y, 3)
                }
                if (data[l].startsWith("Button B")) {
                    val (x, y) = data[l].split(":")[1].split(",").map { it.split("+")[1].toLong() }
                    b = Button(x, y, 1)
                }
                if (data[l].startsWith("Prize")) {
                    val (x, y) = data[l].split(":")[1].split(",").map { it.split("=")[1].toLong() }
                    g = Goal(x, y)
                    yield(Triple(a!!, b!!, g))
                }
            }
        }.toList()
    }


    fun calculate(a: Button, b: Button, g: Goal): Long {
        val v = a.x * b.y - a.y * b.x
        val aT = (g.x * b.y - g.y * b.x) / v
        val bT = (a.x * g.y - a.y * g.x) / v

        if(aT * a.x + bT * b.x != g.x || aT * a.y + bT * b.y != g.y) {
            return 0
        }
        return aT * a.cost + bT* b.cost
    }

    fun part1(input: List<String>): Long {
        val toSolve = parse(input)
        return toSolve.sumOf { (buttonA, buttonB, goal) ->
            calculate(buttonA, buttonB, goal)
        }
    }

    fun part2(input: List<String>): Long {
        val toAdd = 10000000000000
        val toSolve = parse(input).map { (buttonA, buttonB, goal) ->
            Triple(buttonA, buttonB, Goal(x=goal.x + toAdd, y=goal.y + toAdd))
        }

        return toSolve.sumOf { (buttonA, buttonB, goal) ->
            calculate(buttonA, buttonB, goal)
        }
    }

    val trial = readInput("day13")
    val input = readInput("day13", "input")

    check(part1(trial) == 480L)
    part1(input).println()
    part2(input).println()
}
