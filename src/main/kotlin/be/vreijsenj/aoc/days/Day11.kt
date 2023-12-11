package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.math.abs
import kotlin.math.exp
import kotlin.time.measureTime

data class Point(val x: Long, val y: Long, val value: String)

data class Image(val points: List<Point>, var galaxies: List<Point> = emptyList()) {
    companion object {

        @JvmStatic
        fun parse(image: List<String>, rate: Long): Image {
            val (rows, columns) = rasterize(image)

            val points = columns.flatMapIndexed { xIndex, column ->
                column.mapIndexed { yIndex, value ->
                    Point(xIndex.toLong(), yIndex.toLong(), value)
                }
            }

            return Image(points = expand(points, rate))
        }

        private fun expand(points: List<Point>, rate: Long): List<Point> {
            val rows = expand(points.groupBy { it.x }, rate) { point, offset ->
                Point(
                    point.x + offset,
                    point.y,
                    point.value
                )
            }

            val columns = expand(rows.flatMap { it.value }.groupBy { it.y }, rate) { point, offset ->
                Point(
                    point.x,
                    point.y + offset,
                    point.value
                )
            }

            return columns.flatMap { it.value }
        }

        private fun expand(group: Map<Long, List<Point>>, rate: Long, grow: (Point, Long) -> Point): Map<Long, List<Point>> {
            var offset = 0L
            val expanded = mutableMapOf<Long, List<Point>>()

            group.map { entry ->
                val index = entry.key
                val points = entry.value

                if(points.all { it.value == "." }) {
                    offset += rate - 1
                }

                expanded.put(index, points.map { grow(it, offset) })
            }

            return expanded
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

    init {
        galaxies = points.filter { it.value == "#" }
    }
}

object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(11, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of shortest paths between galaxies (pt.1): $resultPartOne")
            println("Sum of shortest paths between galaxies (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Long {
        val image = Image.parse(input, 2)
        return run(image)
    }

    fun runPartTwo(input: List<String>): Long {
        val image = Image.parse(input, 1000000)
        return run(image)
    }

    private fun run(image: Image): Long {
        val pairs = emptyList<Pair<Point, Point>>().toMutableList()

        image.galaxies.onEach { galaxy1 ->
            image.galaxies.onEach { galaxy2 ->
                val pair = Pair(galaxy1, galaxy2)

                val isSelf = galaxy1 == galaxy2
                val isAlreadyIn = pairs.any { pair.first == it.second && pair.second == it.first }

                if(!isSelf && !isAlreadyIn) {
                    pairs.add(pair)
                }
            }
        }

        return pairs.sumOf {
            abs(it.first.x - it.second.x) + abs(it.first.y - it.second.y)
        }
    }
}