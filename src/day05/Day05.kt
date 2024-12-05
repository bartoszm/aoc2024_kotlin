package day05

import utils.readInput
import utils.println

typealias Rule = Pair<Int, Int>

fun Set<Rule>.check(row: List<Int>): Boolean {
    return sequence {
        for(i in row.indices) {
            for(j in i+1 until row.size) {
                yield(row[i] to row[j])
            }
        }
    }.map { it.second to it.first }.none { it in this }
}

fun List<Int>.middle() = this[this.size / 2]

fun main() {
    fun parseRule(l:String) = l.split("|").let { it[0].trim().toInt() to it[1].trim().toInt() }

    fun parseSeq(l: String) = l.split(",").map { it.trim().toInt() }

    fun parse(input: List<String>): Pair<List<Rule>, List<List<Int>>> {
        val rules = input.takeWhile { it.isNotBlank() }.map { parseRule(it) }
        val seq = input.dropWhile { it.isNotBlank() }.drop(1).map { parseSeq(it) }
        return rules to seq
    }

    fun part1(input: List<String>): Int {
        val (rules, toCheck) = parse(input).let{it.first.toSet() to it.second}

        return toCheck.filter { rules.check(it) }
            .sumOf { it.middle() }
    }

    fun part2(input: List<String>): Int {
        val (rules, toCheck) = parse(input).let{it.first.toSet() to it.second}
        val toSort = toCheck.filter { !rules.check(it) }
        val wrongOrders = rules.groupBy { it.second }.mapValues { it.value.map { it.first }.toSet() }

        fun bubleSort(arr: List<Int>): List<Int> {
            val buf = arr.toMutableList()
            var n = arr.size
            var swapped: Boolean
            do {
                swapped = false
                for (i in 1 until n) {
                    if (buf[i - 1] in wrongOrders.getOrDefault(buf[i], emptySet())) {
                        val temp = buf[i]
                        buf[i] = buf[i - 1]
                        buf[i - 1] = temp
                        swapped = true
                    }
                }
            } while (swapped)
            return buf
        }
        return toSort.map { bubleSort(it) }.sumOf { it.middle() }
    }

    val trial = readInput("day05")
    val input = readInput("day05", "input")
    check(part1(trial) == 143)
    part1(input).println()

    check(part2(trial) == 123)
    part2(input).println()
}
