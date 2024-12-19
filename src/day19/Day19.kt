package day19

import utils.println
import utils.readInput
import java.util.concurrent.ConcurrentSkipListMap

data class Input(val components: List<String>, val  patterns: List<String>)

fun parse(input: List<String>): Input {
    val components = input[0].split(",").map { it.trim() }
    val patterns = input.drop(2)
    return Input(components, patterns)
}


fun List<String>.matches(pattern: String): Boolean {
    if(pattern.isEmpty()) return true
    return this
        .filter {c -> pattern.startsWith(c) }
        .any{ c -> this.matches(pattern.removePrefix(c)) }
}

fun countWithMemo(components: List<String>, pattern: String): Long {
    val cache = ConcurrentSkipListMap<String, Long>()

    fun List<String>.count(pattern: String): Long {
        if(pattern.isEmpty()) return 1
        return cache.computeIfAbsent(pattern) {
            this
                .filter {c -> pattern.startsWith(c) }
                .sumOf{ c -> this.count(pattern.removePrefix(c)) }
        }
    }

    return components.count(pattern)
}

fun main() {

    fun part1(data: List<String>): Int {
       val input = parse(data)
       return input.patterns.count { pattern ->
              input.components.matches(pattern)
       }
    }

    fun part2(data: List<String>): Long {
        val input = parse(data)
        return input.patterns.sumOf { pattern ->
            countWithMemo(input.components, pattern)
        }
    }

    val trial = readInput("day19")
    val input = readInput("day19", "input")

    check(part1(trial) == 6)
    part1(input).println()
    check(part2(trial) == 16L)
    part2(input).println()
}
