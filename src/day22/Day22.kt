package day22

import utils.println
import utils.readInput

const val prune = 16777216

const val andFactor = prune - 1

fun next(current: Int): Int {
    val step1 = (current shl  6 xor current) and andFactor
    val step2 = (step1 shr 5 xor step1) and andFactor
    return  (step2 shl 11  xor step2) and andFactor
}

fun generate(s: Int): Sequence<Int> = generateSequence(s) { next(it) }

fun parse(input: List<String>): List<Int> {
    return input.map { it.toInt() }
}

fun part1(input: List<String>): Long {
    val data = parse(input)
    val result = data.map { generate(it).elementAt(2000).toLong() }
    return result.sum()
}

fun differencesFor(start: Int, times: Int = 2000) = generate(start).take(times)
    .map { it % 10 }
    .zipWithNext{ a, b -> (b - a) to b }

fun Sequence<Pair<Int, Int>>.sequences() = this.windowed(4)
    .filter { it.last().second > 0 }
    .map {it.map { it.first } to it.last().second}

fun part2(input: List<String>): Int {
    val data = parse(input)

    val result = data.flatMap { differencesFor(it).sequences().distinctBy { it.first } }
        .groupBy (keySelector =  { it.first }, valueTransform = { it.second })
        .filter { it.value.size > 1 }

    return result.values.maxOfOrNull { it.sum() } ?: -1
}

fun main() {
    val trial = readInput("day22")
    val input = readInput("day22", "input")

    check(part1(trial) == 37327623L)
    part1(input).println()

    check(part2(listOf("1","2","3", "2024")) == 23)
    part2(input).println()
}