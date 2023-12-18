package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.math.abs
import kotlin.time.measureTime

object shoelace {
    fun area(edges: List<ColorEdge>): Long {
        val perimeter = edges.flatMap { listOf(it.start, it.end) }

        val result = perimeter.indices.sumOf { i ->
            if(perimeter.size > i + 1) {
                val length = abs(perimeter[i].x - perimeter[i + 1].x) + abs(perimeter[i].y - perimeter[i + 1].y)

                perimeter[i].x * perimeter[i + 1].y - perimeter[i + 1].x * perimeter[i].y + length
            } else {
                0L
            }
        }

        return result / 2 + 1
    }
}

enum class DigDirection(val isValue: (String) -> Boolean, val next: (Hole) -> Hole) {
    RIGHT({ it == "R" || it == "0" }, { origin -> Hole(origin.x + 1, origin.y) }),
    DOWN({ it == "D" || it == "1" }, { origin -> Hole(origin.x, origin.y + 1) }),
    LEFT({ it == "L" || it == "2" }, { origin -> Hole(origin.x - 1, origin.y)}),
    UP({ it == "U" || it == "3" }, { origin -> Hole(origin.x, origin.y - 1) });

    companion object {
        @JvmStatic
        fun parse(value: String) = entries.first { it.isValue(value) }
    }
}

data class Hole(val x: Long, val y: Long)

data class ColorEdge(val start: Hole, val end: Hole, val color: String) {
    companion object {
        @JvmStatic
        fun parse(line: String, start: Hole): ColorEdge {
            var previous = start
            val (direction, length, color) = line.split(" ")

            (1..length.toInt()).onEach {
                previous = DigDirection.parse(direction).next(previous)
            }

            return ColorEdge(start, previous, color)
        }

        @JvmStatic
        fun parseFromHex(line: String, start: Hole): ColorEdge {
            val (_, _, hex) = line.split(" ").map { it.replace("#", "") }

            val length = hex.substring(0, 5).toLong(radix = 16)
            val direction = hex.last().toString()

            var previous = start

            (1..length).onEach {
                previous = DigDirection.parse(direction).next(previous)
            }

            return ColorEdge(start, previous, hex)
        }
    }
}

data class DigPlan(val edges: List<ColorEdge>) {
    companion object {
        @JvmStatic
        fun parse(input: List<String>, fromHexValue: Boolean = false): DigPlan {
            var start = Hole(0, 0)
            val edges = input
                .map { it.replace("""[()]""".toRegex(), "") }
                .map {
                    val edge = if(fromHexValue) {
                        ColorEdge.parseFromHex(it, start)
                    } else {
                        ColorEdge.parse(it, start)
                    }

                    start = edge.end
                    edge
                }

            return DigPlan(edges)
        }
    }
}
object Day18 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(18, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Amount of cubic meter (pt.1): $resultPartOne")
            println("Amount of cubic meter (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Long {
        val plan = DigPlan.parse(input)
        return shoelace.area(plan.edges)
    }

    fun runPartTwo(input: List<String>): Long {
        val plan = DigPlan.parse(input, fromHexValue = true)
        return shoelace.area(plan.edges)
    }
}