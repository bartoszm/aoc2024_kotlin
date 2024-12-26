package day23

import utils.combine
import utils.println
import utils.readInput
import utils.toPair

fun parse(input: List<String>) = input
    .map{ it.split("-").toPair() }
    .flatMap { sequenceOf(it.first to it.second, it.second to it.first) }
    .groupBy(keySelector = {it.first} , valueTransform = {it.second})
    .mapValues { it.value.toSet() }

fun Map<String, Set<String>>.cycle3(data: Map<String, Set<String>> = this) = this.flatMap { (k,v) ->
    v.toList().combine()
        .filter { (a,b) -> data[a]?.contains(b) ?: false }
        .map { (a,b) -> setOf(a,b,k) }
}.distinct()

fun part1(input: List<String>): Int {
    val data = parse(input)
    val tStart = data.filter { it.key.startsWith("t") }
    return tStart.cycle3(data).size
}

fun part2(input: List<String>): String {
    val data = parse(input)

    fun enlarge(cycle: Set<String>): Set<String>? {
         return data.filter{(_,v) -> v.containsAll(cycle)}
             .map {(k,_) -> setOf(k) + cycle }
             .firstOrNull()
    }

    val allCycle3 = data.cycle3()

    var prev: List<Set<String>>
    var next = allCycle3
    do {
        prev = next
        next = prev.mapNotNull { enlarge(it) }.distinct()
    } while(next.isNotEmpty())

    return prev[0].sorted().joinToString(",")
}

fun main() {
    val trial = readInput("day23")
    val input = readInput("day23", "input")

    check(part1(trial) == 7)
    part1(input).println()
    check(part2(trial) == "co,de,ka,ta")
    part2(input).println()
}