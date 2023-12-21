package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.Grid
import be.vreijsenj.aoc.utils.Point
import be.vreijsenj.aoc.utils.PuzzleUtils
import org.codehaus.groovy.runtime.DefaultGroovyMethods.power
import kotlin.time.measureTime

enum class TileType {
    PLOT, ROCK;

    companion object {
        @JvmStatic
        fun of(char: Char): TileType {
            return when(char) {
                '#' -> ROCK
                else -> PLOT
            }
        }
    }
}

data class GardenMap(val start: Point, override val points: List<Point>, val values: Map<Point, TileType>): Grid(points) {

    companion object {
        @JvmStatic
        fun parse(input: List<String>): GardenMap {
            var start = Point(0, 0)
            val values = mutableMapOf<Point, TileType>()

            val points = rasterize(input) { xIndex, yIndex, char ->
                val point = Point(xIndex, yIndex)

                if(char == 'S') {
                    start = point
                }

                values[point] = TileType.of(char)

                point
            }

            return GardenMap(start, points, values)
        }
    }

    fun walk(point: Point): List<Point> {
        return point.neighbours().filter {
            values[it.relative(xMax, yMax)] == TileType.PLOT
        }
    }
}

object Day21 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(21, 1)

            val resultPartOne = runPartOne(input, steps = 64)
            val resultPartTwo = runPartTwo(input, steps = 202300)

            println("Sum of high and low pulses (pt.1): $resultPartOne")
            println("Sum of high and low pulses (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>, steps: Int): Long {
        val map = GardenMap.parse(input)

        var queue = setOf(map.start)

        (1..steps).onEach {
            queue = queue.flatMap { map.walk(it) }.toSet()
        }

        return queue.size.toLong()
    }

    fun runPartTwo(input: List<String>, steps: Long): Long {
        val a = runPartOne(input, 65)
        val b = runPartOne(input, 65 + 131)
        val c = runPartOne(input, 65 + (131 * 2))

        // Since there are no rocks in row/col of starting point, we (apparently) know it grows quadratically...
        // We can use wolframalpha to calculate the perfect fit formula, given the above values a, b, c
        // https://www.wolframalpha.com/input?i=quadratic+fit+calculator
        // -------------------------------------------------------------
        // data set of {x,y} values: {{0,a},{1,b}, {2,c}}

        return 3884L + (15394L * steps) + (15286L * (steps * steps))
    }
}