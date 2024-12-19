package day17

import utils.println
import utils.readInput

class Register(var value: Int = 0) {
    override fun toString(): String {
        return "$value"
    }
}

data class Computer(val registers: List<Register>, val program: List<Int>) {
    operator fun get(c: Char) = when(c) {
            'a' -> registers[0].value
            'b' -> registers[1].value
            'c' -> registers[2].value
            else -> error("Invalid register")
    }
    operator fun set(c:Char, value: Int) {
        when(c) {
            'a' -> registers[0].value = value
            'b' -> registers[1].value = value
            'c' -> registers[2].value = value
            else -> error("Invalid register")
        }
    }
}

fun parse(input: List<String>): Computer {
    fun register(s: String): Register {
        return Register(s.split(":")[1].trim().toInt())
    }

    val resisters = (0..2).map { register(input[it]) }
    val instructions = input[4].split(":")[1].split(",").map { it.trim().toInt() }
    return Computer(resisters, instructions)
}



fun main() {

    fun part1(input: List<String>): String {
        fun Computer.combo(i: Int): Int {
            return when(i) {
                in 0..3 -> i
                4 -> this['a']
                5 -> this['b']
                6 -> this['c']
                else -> error("Invalid combo")
            }
        }

        fun Computer.xdv(target: Char, pointer: Int) {
            val d  = combo(this.program[pointer + 1])
            val denom = if (d == 0) 1 else 2 shl (d - 1)

            this[target] = this['a'] / denom
        }

        fun Computer.exec() : Sequence<Int> {
            var pointer = 0
            return sequence {
                val comp = this@exec
                while (pointer < comp.program.size) {
                    val instruction = program[pointer]
                    when (instruction) {
                        0 -> { xdv('a', pointer) }
                        1 -> { comp['b'] = comp['b'] xor program[pointer + 1] }
                        2 -> { comp['b'] = comp.combo(program[pointer + 1]) and 7 }
                        3 -> {
                            if (comp['a'] > 0) {
                                pointer = program[pointer + 1]
                                continue
                            }
                        }
                        4 -> { comp['b'] = comp['b'] xor comp['c'] }
                        5 -> { yield(comp.combo(program[pointer + 1]) and 7) }
                        6 -> { xdv('b', pointer) }
                        7 -> { xdv('c', pointer) }
                    }
                    pointer += 2
                }
            }
        }
        val computer = parse(input)
        val res = computer.exec().toList()
        println("Register A = ${computer['a']}")
        println("Register B = ${computer['b']}")
        println("Register C = ${computer['c']}")
        return res.joinToString(",")
    }

    fun part2(input: List<String>): Int {
       TODO()
    }

    val trial = readInput("day17")
    val input = readInput("day17", "input")

    check(part1(trial) == "4,6,3,5,6,3,5,2,1,0")
    part1(input).println()
//
//    check(part2(trial) == 45)
//    part2(input).println()

}
