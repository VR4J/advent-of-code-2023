package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.math.max
import kotlin.time.measureTime

enum class BeamDirection {
    UP, LEFT, RIGHT, DOWN
}

val REFLECT_CACHE = mutableListOf<Pair<Location, BeamDirection>>()

data class Contraption(val points: List<Location>, val mirrors: Map<Location, String>, val xMax: Int, val yMax:Int) {

    companion object {
        @JvmStatic
        fun parse(input: List<String>): Contraption {
            val (rows, columns) = rasterize(input)

            val mirrors = mutableMapOf<Location, String>()
            val points = columns.flatMapIndexed { xIndex, column ->
                column.mapIndexed { yIndex, value ->
                    val point = Location(xIndex, yIndex)

                    if(value in listOf("\\", "/", "-", "|")) {
                        mirrors[point] = value
                    }

                    point
                }
            }

            return Contraption(points = points, mirrors = mirrors, points.maxOf { it.x }, points.maxOf { it.y })
        }

        private fun rasterize(input: List<String>): Pair<List<List<String>>, List<List<String>>> {
            val rows = input.map { it.chunked(1) }

            val columns = rows.first.indices
                .map { xIndex ->
                    rows.map { it[xIndex] }
                }

            return Pair(rows, columns)
        }
    }

    fun energize(start: Location, direction: BeamDirection) {
        REFLECT_CACHE.add(
            Pair(start, direction)
        )

        reflect(start, direction)
    }

    fun reflect(position: Location, direction: BeamDirection) {
        val value = mirrors[position] ?: "."
        val next = getNextPosition(position, value, direction)

        next.onEach { (location, direction) ->
            val isAlreadyEnergized = REFLECT_CACHE.any { it.first == location && it.second == direction }

            if(! isAlreadyEnergized) {
                REFLECT_CACHE.add(
                    Pair(location, direction)
                )

                reflect(location, direction)
            }
        }
    }

    private fun getNextPosition(position: Location, value: String, direction: BeamDirection): List<Pair<Location, BeamDirection>> {
        var result = emptyList<Pair<Location, BeamDirection>>()

        if(value == ".") {
            val next = when(direction) {
                BeamDirection.UP -> Location(position.x, position.y - 1)
                BeamDirection.RIGHT -> Location(position.x + 1, position.y)
                BeamDirection.LEFT -> Location(position.x - 1, position.y)
                BeamDirection.DOWN -> Location(position.x, position.y + 1)
            }

            result = listOf(
                Pair(next, direction)
            )
        }

        if(value == "\\") {
            val next = when(direction) {
                BeamDirection.UP -> Pair(Location(position.x - 1, position.y), BeamDirection.LEFT)
                BeamDirection.RIGHT -> Pair(Location(position.x, position.y + 1), BeamDirection.DOWN)
                BeamDirection.LEFT -> Pair(Location(position.x, position.y - 1), BeamDirection.UP)
                BeamDirection.DOWN -> Pair(Location(position.x + 1, position.y), BeamDirection.RIGHT)
            }

            result = listOf(next)
        }

        if(value == "/") {
            val next = when(direction) {
                BeamDirection.UP -> Pair(Location(position.x + 1, position.y), BeamDirection.RIGHT)
                BeamDirection.RIGHT -> Pair(Location(position.x, position.y - 1), BeamDirection.UP)
                BeamDirection.LEFT -> Pair(Location(position.x, position.y + 1), BeamDirection.DOWN)
                BeamDirection.DOWN -> Pair(Location(position.x - 1, position.y), BeamDirection.LEFT)
            }

            result = listOf(next)
        }

        if(value == "-") {
            val next = when(direction) {
                BeamDirection.DOWN -> listOf(
                    Pair(Location(position.x - 1, position.y), BeamDirection.LEFT),
                    Pair(Location(position.x + 1, position.y), BeamDirection.RIGHT)
                )
                BeamDirection.UP -> listOf(
                    Pair(Location(position.x - 1, position.y), BeamDirection.LEFT),
                    Pair(Location(position.x + 1, position.y), BeamDirection.RIGHT)
                )
                BeamDirection.RIGHT -> listOf(Pair(Location(position.x + 1, position.y), direction))
                BeamDirection.LEFT -> listOf(Pair(Location(position.x - 1, position.y), direction))
            }

            result = next
        }

        if(value == "|") {
            val next = when(direction) {
                BeamDirection.DOWN -> listOf(Pair(Location(position.x, position.y + 1), direction))
                BeamDirection.UP -> listOf(Pair(Location(position.x, position.y - 1), direction))
                BeamDirection.RIGHT -> listOf(
                    Pair(Location(position.x, position.y - 1), BeamDirection.UP),
                    Pair(Location(position.x, position.y + 1), BeamDirection.DOWN)
                )
                BeamDirection.LEFT -> listOf(
                    Pair(Location(position.x, position.y - 1), BeamDirection.UP),
                    Pair(Location(position.x, position.y + 1), BeamDirection.DOWN)
                )
            }

            result = next
        }

        return result.filter { (location, direction) ->
            (direction == BeamDirection.UP && location.y >= 0)
                    || (direction == BeamDirection.DOWN && location.y <= yMax)
                    || (direction == BeamDirection.LEFT && location.x >= 0)
                    || (direction == BeamDirection.RIGHT && location.x <= xMax)
        }
    }
}

object Day16 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(16, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Total load on north support beam (pt.1): $resultPartOne")
            println("Total load on north support beam (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val contraption = Contraption.parse(input)
        return run(contraption, 0, 0, listOf(BeamDirection.RIGHT))
    }

    fun runPartTwo(input: List<String>): Int {
        val contraption = Contraption.parse(input)

        var max = 0

        val edges = contraption.points.filter { point ->
            point.x == 0 || point.x == contraption.xMax || point.y == 0 || point.y == contraption.yMax
        }

        edges.onEach { (x, y) ->
            var directions = listOf<BeamDirection>()

            // top left corner
            if(x == 0 && y == 0) {
                directions = listOf(BeamDirection.DOWN, BeamDirection.RIGHT)

            // top right corner
            } else if(x == contraption.xMax && y == 0) {
                directions = listOf(BeamDirection.DOWN, BeamDirection.LEFT)

            // bottom left corner
            } else if(y == contraption.yMax && x == 0) {
                directions = listOf(BeamDirection.RIGHT, BeamDirection.UP)

            // bottom right corner
            } else if(y == contraption.yMax && x == contraption.xMax) {
                directions = listOf(BeamDirection.LEFT, BeamDirection.UP)

            // left column
            } else if(x == 0) {
                directions = listOf(BeamDirection.RIGHT)

            // top row
            } else if(y == 0) {
                directions = listOf(BeamDirection.DOWN)

            // right column
            } else if(x == contraption.xMax) {
                directions = listOf(BeamDirection.LEFT)

            // bottom row
            } else if(y == contraption.yMax) {
                directions = listOf(BeamDirection.UP)
            }

            val result = run(contraption, x, y, directions)

            max = max(max, result)
        }

        return max
    }

    private fun run(contraption: Contraption, x: Int, y: Int, directions: List<BeamDirection>): Int {
        var result = 0
        val start = Location(x, y)

        directions.onEach { direction ->
            REFLECT_CACHE.clear()

            contraption.energize(start, direction)

            result = max(result, REFLECT_CACHE.distinctBy { it.first }.size)
        }

        return result
    }
}