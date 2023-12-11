package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.Double.Companion.MAX_VALUE
import kotlin.Double.Companion.MIN_VALUE
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime
data class Edge(val start: Position, val end: Position) {

    /**
     * Ray casting implementation based on polygon edges.
     * https://en.wikipedia.org/wiki/Ray_casting
     */
    operator fun invoke(position: Position) : Boolean {
        // Swap vertical edge
        if(start.y > end.y) {
            return Edge(end, start).invoke(position)
        }

        // When vertical edge is exactly on start or end, move it slightly
        if(position.y == start.y || position.y == end.y) {
            return invoke(Position(position.x, position.y + epsilon))
        }

        // When outside vertical range, and is after horizontal range
        if(position.y > end.y || position.y < start.y || position.x > max(start.x, end.x)) {
            return false
        }

        // When inside vertical range, and is before horizontal range
        if(position.x < min(start.x, end.x)) {
            return true
        }

        // Areas
        val blue = if(abs(start.x - position.x) > MIN_VALUE) (position.y - start.y) / (position.x - start.x) else MAX_VALUE
        val red = if(abs(start.x - end.x) > MIN_VALUE) (end.y - start.y) / (end.x - start.x) else MAX_VALUE

        return blue >= red
    }

    private val epsilon = 0.00001
}
data class Position(val x: Double, val y: Double) {

    fun surrounding(): List<Position> {
        return listOf(top(), bottom(), left(), right())
    }

    fun top(): Position {
        return Position(x, y - 1)
    }

    fun bottom(): Position {
        return Position(x, y + 1)
    }

    fun left(): Position {
        return Position(x - 1, y)
    }

    fun right(): Position {
        return Position(x + 1, y)
    }
}
enum class Pipe(val indicator: Char, val next: (Position, Position) -> Position?) {
    START('S', { _,_ -> null }),
    TOP_BOTTOM('|', { previous, current ->
        when(previous) {
            current.top() -> current.bottom()
            current.bottom() -> current.top()
            else -> null
        }
    }),
    LEF_RIGHT('-',{ previous, current ->
        when(previous) {
            current.left() -> current.right()
            current.right() -> current.left()
            else -> null
        }
    }),
    TOP_RIGHT('L', { previous, current ->
        when(previous) {
            current.top() -> current.right()
            current.right() -> current.top()
            else -> null
        }
    }),
    TOP_LEFT('J', { previous, current ->
        when(previous) {
            current.top() -> current.left()
            current.left() -> current.top()
            else -> null
        }
    }),
    LEFT_BOTTOM('7', { previous, current ->
        when(previous) {
            current.left() -> current.bottom()
            current.bottom() -> current.left()
            else -> null
        }
    }),
    RIGHT_BOTTOM('F', { previous, current ->
        when(previous) {
            current.right() -> current.bottom()
            current.bottom() -> current.right()
            else -> null
        }
    });

    companion object {
        @JvmStatic
        fun parse(indicator: Char): Pipe? {
            return entries.find { it.indicator == indicator }
        }
    }
}
data class Sketch(val start: Position, val tiles: Map<Position, Pipe?>) {
    companion object {
        @JvmStatic
        fun parse(input: List<String>): Sketch {
            val tiles = input.indices.asSequence()
                .flatMap { yIndex ->
                    val line = input[yIndex]

                    line.mapIndexed { xIndex, indicator ->
                            val pipe = Pipe.parse(indicator)
                            val position = Position(xIndex.toDouble(), yIndex.toDouble())

                            Pair(position, pipe)
                        }
                }
                .toMap()

            return Sketch(
                start = tiles.entries.first { it.value?.indicator == 'S' }.key,
                tiles = tiles
            )
        }
    }

    fun loop(): List<Position> {
        val loops = start.surrounding()
            .map { listOf(it).toMutableList() }

        for(loop in loops) {
            var previous = start
            var current = loop.first

            while(true) {
                val pipe = tiles[current] ?: break
                val next = pipe.next(previous, current) ?: break

                loop.add(next)

                if(next == start) {
                    // We have a loop!
                    loop.add(0, start)
                    return loop
                }

                previous = current
                current = next
            }
        }

        throw IllegalStateException("Unable to find loop in sketch")
    }
}
object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(10, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Farthest point in loop (pt.1): $resultPartOne")
            println("Enclosed tiles in loop (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val sketch = Sketch.parse(input)
        return sketch.loop().size / 2
    }

    fun runPartTwo(input: List<String>): Int {
        val sketch = Sketch.parse(input)
        val loop = sketch.loop()
        val edges = getEdges(loop)

        return sketch.tiles
            .filter { ! loop.contains(it.key) }
            .filter { tile -> edges.count { it(tile.key) } % 2 != 0 }
            .size
    }

    private fun getEdges(loop: List<Position>): List<Edge> {
        val edges = emptyList<Edge>().toMutableList()

        var eStart = loop.first
        var direction = ""

        loop.zipWithNext { current, next ->
            if(direction == "y") {
                if(next.x != current.x) {
                    val edge = Edge(eStart, current)

                    edges.add(edge)

                    eStart = current
                }
            }

            if(direction == "x") {
                if(next.y != current.y) {
                    val edge = Edge(eStart, current)

                    edges.add(edge)

                    eStart = current
                }
            }

            if(next.x != current.x) {
                direction = "x"
            }

            if(next.y != current.y) {
                direction = "y"
            }
        }

        val edge = Edge(eStart, loop.last)

        edges.add(edge)

        return edges
    }
}