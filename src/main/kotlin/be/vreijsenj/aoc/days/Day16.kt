package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.Direction
import be.vreijsenj.aoc.utils.Grid
import be.vreijsenj.aoc.utils.Point
import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.math.max
import kotlin.time.measureTime

val REFLECT_CACHE = mutableListOf<Pair<Point, Direction>>()

data class Contraption(override val points: List<Point>, val mirrors: Map<Point, Char>): Grid(points) {
    companion object {
        @JvmStatic
        fun parse(input: List<String>): Contraption {
            val mirrors = mutableMapOf<Point, Char>()

            val points = rasterize(input) { xIndex, yIndex, char ->
                val point = Point(xIndex, yIndex)

                if (char in listOf('\\', '/', '-', '|')) {
                    mirrors[point] = char
                }

                point
            }

            return Contraption(points = points, mirrors = mirrors)
        }
    }

    fun energize(start: Point, direction: Direction) {
        REFLECT_CACHE.add(
            Pair(start, direction)
        )

        reflect(start, direction)
    }

    private fun reflect(point: Point, direction: Direction) {
        val value = mirrors[point] ?: '.'
        val next = getNextPosition(point, value, direction)

        next.onEach { (location, direction) ->
            val isAlreadyEnergized = REFLECT_CACHE.any { it.first == location && it.second == direction }

            if (!isAlreadyEnergized) {
                REFLECT_CACHE.add(
                    Pair(location, direction)
                )

                reflect(location, direction)
            }
        }
    }

    private fun getNextPosition(point: Point, value: Char, direction: Direction): List<Pair<Point, Direction>> {
        var result = emptyList<Pair<Point, Direction>>()

        if(value == '.') {
            result = listOf(
                Pair(point.next(direction), direction)
            )
        }

        if(value == '\\') {
            val next = when(direction) {
                Direction.UP -> Pair(point.left(), Direction.LEFT)
                Direction.RIGHT -> Pair(point.bottom(), Direction.DOWN)
                Direction.LEFT -> Pair(point.top(), Direction.UP)
                Direction.DOWN -> Pair(point.right(), Direction.RIGHT)
            }

            result = listOf(next)
        }

        if(value == '/') {
            val next = when(direction) {
                Direction.UP -> Pair(point.right(), Direction.RIGHT)
                Direction.RIGHT -> Pair(point.top(), Direction.UP)
                Direction.LEFT -> Pair(point.bottom(), Direction.DOWN)
                Direction.DOWN -> Pair(point.left(), Direction.LEFT)
            }

            result = listOf(next)
        }

        if(value == '-') {
            val next = when(direction) {
                Direction.DOWN -> listOf(
                    Pair(point.left(), Direction.LEFT),
                    Pair(point.right(), Direction.RIGHT)
                )
                Direction.UP -> listOf(
                    Pair(point.left(), Direction.LEFT),
                    Pair(point.right(), Direction.RIGHT)
                )
                else -> listOf(
                    Pair(point.next(direction), direction)
                )
            }

            result = next
        }

        if(value == '|') {
            val next = when(direction) {
                Direction.RIGHT -> listOf(
                    Pair(point.top(), Direction.UP),
                    Pair(point.bottom(), Direction.DOWN)
                )
                Direction.LEFT -> listOf(
                    Pair(point.top(), Direction.UP),
                    Pair(point.bottom(), Direction.DOWN)
                )
                else -> listOf(
                    Pair(point.next(direction), direction)
                )
            }

            result = next
        }

        return result.filter { (point, _) -> point in this }
    }
}

object Day16 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(16, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of all energized tiles (pt.1): $resultPartOne")
            println("Sum of max energized tiles (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val contraption = Contraption.parse(input)
        return run(contraption, 0, 0, listOf(Direction.RIGHT))
    }

    fun runPartTwo(input: List<String>): Int {
        val contraption = Contraption.parse(input)

        var max = 0

        val edges = contraption.points.filter { point ->
            point.x == 0 || point.x == contraption.xMax || point.y == 0 || point.y == contraption.yMax
        }

        edges.onEach { (x, y) ->
            var directions = listOf<Direction>()

            // top left corner
            if(x == 0 && y == 0) {
                directions = listOf(Direction.DOWN, Direction.RIGHT)

            // top right corner
            } else if(x == contraption.xMax && y == 0) {
                directions = listOf(Direction.DOWN, Direction.LEFT)

            // bottom left corner
            } else if(y == contraption.yMax && x == 0) {
                directions = listOf(Direction.RIGHT, Direction.UP)

            // bottom right corner
            } else if(y == contraption.yMax && x == contraption.xMax) {
                directions = listOf(Direction.LEFT, Direction.UP)

            // left column
            } else if(x == 0) {
                directions = listOf(Direction.RIGHT)

            // top row
            } else if(y == 0) {
                directions = listOf(Direction.DOWN)

            // right column
            } else if(x == contraption.xMax) {
                directions = listOf(Direction.LEFT)

            // bottom row
            } else if(y == contraption.yMax) {
                directions = listOf(Direction.UP)
            }

            val result = run(contraption, x, y, directions)

            max = max(max, result)
        }

        return max
    }

    private fun run(contraption: Contraption, x: Int, y: Int, directions: List<Direction>): Int {
        var result = 0
        val start = Point(x, y)

        directions.onEach { direction ->
            REFLECT_CACHE.clear()

            contraption.energize(start, direction)

            result = max(result, REFLECT_CACHE.distinctBy { it.first }.size)
        }

        return result
    }
}