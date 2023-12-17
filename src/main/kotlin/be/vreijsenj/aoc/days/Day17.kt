package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import java.util.*
import kotlin.math.min
import kotlin.time.measureTime

enum class StepDirection(val next: (List<Point>, Point) -> Point?) {
    LEFT({ points, point -> points.find { it.x == point.x - 1 && it.y == point.y } }),
    RIGHT({ points, point -> points.find { it.x == point.x + 1 && it.y == point.y } }),
    UP({ points, point -> points.find { it.x == point.x && it.y == point.y - 1 } }),
    DOWN({ points, point -> points.find { it.x == point.x && it.y == point.y + 1 } });
}

data class CityBlock(val point: Point, val direction: StepDirection, val n: Int) {

    fun neighbours(points: List<Point>): List<CityBlock> {
        return StepDirection.entries.mapNotNull loop@ {
            if(
                (direction == StepDirection.RIGHT && it == StepDirection.LEFT)
                || (direction == StepDirection.LEFT && it == StepDirection.RIGHT)
                || (direction == StepDirection.UP && it == StepDirection.DOWN)
                || (direction == StepDirection.DOWN && it == StepDirection.UP)
            ) return@loop null

            if(it == direction && n >= 3) return@loop null

            val neighbour = it.next(points, point) ?: return@loop null
            val count = if(it == direction) n + 1 else 1

            CityBlock(neighbour, it, count)
        }
    }

    fun ultra(points: List<Point>): List<CityBlock> {
        // Must at least walk 4 blocks
        if(n < 4) {
            val next = direction.next(points, point) ?: return emptyList()
            return listOf(CityBlock(next, direction, n + 1))
        }

        return StepDirection.entries.mapNotNull loop@ {
            if(
                (direction == StepDirection.RIGHT && it == StepDirection.LEFT)
                || (direction == StepDirection.LEFT && it == StepDirection.RIGHT)
                || (direction == StepDirection.UP && it == StepDirection.DOWN)
                || (direction == StepDirection.DOWN && it == StepDirection.UP)
            ) return@loop null

            // Can not continue after 10 blocks
            if(n >= 10 && it == direction) return@loop null

            val neighbour = it.next(points, point) ?: return@loop null
            val count = if (it == direction) n + 1 else 1

            CityBlock(neighbour, it, count)
        }
    }
}

data class CityBlockMap(val points: List<Point>, val start: Point, val end: Point) {
    companion object {

        @JvmStatic
        fun parse(input: List<String>): CityBlockMap {
            val (rows, columns) = rasterize(input)

            val points = columns.flatMapIndexed { xIndex, column ->
                column.mapIndexed { yIndex, value ->
                    Point(xIndex.toLong(), yIndex.toLong(), value)
                }
            }

            val xMax = points.maxOf { it.x }
            val yMax = points.maxOf { it.y }

            return CityBlockMap(points, points.first { it.x == 0L && it.y == 0L }, points.first { it.x == xMax && it.y == yMax })
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
}

data class Seen(val distance: Int, val previous: CityBlock?)
data class Scored(val block: CityBlock, val score: Int): Comparable<Scored> {
    override fun compareTo(other: Scored): Int = (score).compareTo(other.score)
}

fun getShortestPath(start: CityBlock, neighbours: (CityBlock) -> List<CityBlock>, isFinished: (CityBlock) -> Boolean): List<CityBlock> {
    val toVisit = PriorityQueue(
        listOf(Scored(start, 0))
    )

    var last: CityBlock? = null

    val visited: MutableMap<CityBlock, Seen> = mutableMapOf(start to Seen(0, null))

    while (last == null) {
        val (block, score) = toVisit.remove()

        last = if (isFinished(block)) block else null

        val nextPoints = neighbours(block)
            .filter { it !in visited }
            .map { next -> Scored(next, score + block.point.value.toInt()) }

        toVisit.addAll(nextPoints)
        visited.putAll(nextPoints.associate { it.block to Seen(it.score, block) })
    }

    return getPath(last, visited, emptyList())
}

private fun getPath(last: CityBlock, result: Map<CityBlock, Seen>, path: List<CityBlock>): List<CityBlock> {
    val previous = result[last]?.previous

    return if (previous == null) {
        listOf(last) + path
    } else {
        getPath(previous, result, listOf(last) + path)
    }
}


object Day17 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(17, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Least heat loss (pt.1): $resultPartOne")
            println("Least heat loss (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val map = CityBlockMap.parse(input)
        val start = CityBlock(map.start, StepDirection.RIGHT, 0)

        val path = getShortestPath(
            start,
            { it.neighbours(map.points) },
            { it.point == map.end }
        )

        return path.sumOf { it.point.value.toInt() } - map.start.value.toInt()
    }

    fun runPartTwo(input: List<String>): Int {
        val map = CityBlockMap.parse(input)
        val sDown = CityBlock(map.start, StepDirection.DOWN, 0)
        val sRight = CityBlock(map.start, StepDirection.RIGHT, 0)

        return min(runStartingFrom(map, sRight), runStartingFrom(map, sDown))
    }

    private fun runStartingFrom(map: CityBlockMap, start: CityBlock): Int {
        val path = getShortestPath(
            start,
            { it.ultra(map.points) },
            { it.point == map.end && it.n >= 4}
        )

        return path.sumOf { it.point.value.toInt() } - map.start.value.toInt()
    }
}