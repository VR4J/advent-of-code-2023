package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.Grid
import be.vreijsenj.aoc.utils.Point
import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

val NEIGHBOUR_CACHE = mutableMapOf<Pair<Point, Boolean>, List<Point>>()

var length = 0

enum class ForestTileType {
    FOREST, PATH, SLOPE_UP, SLOPE_LEFT, SLOPE_RIGHT, SLOPE_DOWN;

    companion object {
        @JvmStatic
        fun of(char: Char): ForestTileType {
            return when(char) {
                '#' -> FOREST
                '^' -> SLOPE_UP
                '<' -> SLOPE_LEFT
                '>' -> SLOPE_RIGHT
                'v' -> SLOPE_DOWN
                else -> PATH
            }
        }
    }
}

data class ForestMap(override val points: List<Point>, val values: Map<Point, ForestTileType>): Grid(points) {

    companion object {
        @JvmStatic
        fun parse(input: List<String>): ForestMap {
            val values = mutableMapOf<Point, ForestTileType>()

            val points = rasterize(input) { xIndex, yIndex, char ->
                val point = Point(xIndex, yIndex)

                values[point] = ForestTileType.of(char)

                point
            }

            return ForestMap(points = points, values = values)
        }
    }

    fun neighbours(point: Point, slippery: Boolean = false): List<Point> {
        val signature = Pair(point, slippery)

        if(signature in NEIGHBOUR_CACHE) {
            return NEIGHBOUR_CACHE[signature] !!
        }

        val value = values[point]

        val neighbours = point.neighbours()
            .mapNotNull { neighbour ->
                if(slippery) {
                    if(value == ForestTileType.SLOPE_UP) {
                        point.top()
                    } else if(value == ForestTileType.SLOPE_LEFT) {
                        point.left()
                    } else if(value == ForestTileType.SLOPE_RIGHT) {
                        point.right()
                    } else if(value == ForestTileType.SLOPE_DOWN) {
                        point.bottom()
                    } else {
                        val nValue = values[neighbour]

                        if (nValue == null || nValue == ForestTileType.FOREST) {
                            null
                        } else {
                            neighbour
                        }
                    }
                } else {
                    val nValue = values[neighbour]

                    if (nValue == null || nValue == ForestTileType.FOREST) {
                        null
                    } else {
                        neighbour
                    }
                }

            }
            .distinct()

        NEIGHBOUR_CACHE[signature] = neighbours

        return neighbours
    }
}

fun getPaths(start: Point, neighbours: (Point) -> List<Point>, end: Point): List<List<Point>> {
    return traverse(start, neighbours, mutableListOf(), end)
}

/*
 * Whenever we encounter a split, we need to start traversing both paths.
 * x=3, y=5 -> traverse(Point(x=4, y=5), traversed, end)
 *          -> traverse(Point(x=3, y=6), traversed, end)
 *
 * Extract traverse method that does the walking:
 * - Check neighbours, if not visited -> add to visited and traverse to neighbour
 */
private fun traverse(point: Point, neighbours: (Point) -> List<Point>, traversed: MutableList<Point>, end: Point): List<List<Point>> {
    if(point == end) {
        if(traversed.size > length) {
            length = traversed.size
            print("Longest Route: " + length + "\r")
        }

        return listOf(traversed)
    }

    traversed.add(point)

    return neighbours(point)
        .filter { it !in traversed }
        .flatMap { traverse(it, neighbours, traversed.toMutableList(), end) }
}

object Day23 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(23, 1)

            val resultPartOne = runPartOne(input)

            println("Longest path (pt.1): $resultPartOne")

            val resultPartTwo = runPartTwo(input)

            println("Longest path (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val map = ForestMap.parse(input)
        val start = map.points.first { it.y == map.yMin && map.values[it] == ForestTileType.PATH }
        val end = map.points.first { it.y == map.yMax && map.values[it] == ForestTileType.PATH }

        val paths = getPaths(
            start,
            { map.neighbours(it, slippery = true) },
            end,
        )

        return paths.maxOf { it.size }
    }

    fun runPartTwo(input: List<String>): Int {
        val map = ForestMap.parse(input)
        val start = map.points.first { it.y == map.yMin && map.values[it] == ForestTileType.PATH }
        val end = map.points.first { it.y == map.yMax && map.values[it] == ForestTileType.PATH }

        val paths = getPaths(
            start,
            { map.neighbours(it, slippery = false) },
            end,
        )

        return paths.maxOf { it.size }
    }
}