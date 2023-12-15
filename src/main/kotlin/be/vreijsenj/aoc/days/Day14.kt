package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.collections.Set
import kotlin.time.measureTime

val TILT_CACHE = mutableMapOf<Pair<Set<Location>, TiltDirection>, Platform>()

enum class TiltDirection {
    NORTH, WEST, SOUTH, EAST
}
enum class RockType {
    ROUND, SQUARE;

    companion object {
        @JvmStatic
        fun of(value: Char): RockType {
            return when(value) {
                'O' -> ROUND
                '#' -> SQUARE
                else -> throw IllegalStateException("Unidentified rock found")
            }
        }
    }
}

data class Surrounding(val top: RockType?, val bottom: RockType?, val left: RockType?, val right: RockType?)

data class Location(val x: Int, val y: Int)

data class Rock(var x: Int, var y: Int, val type: RockType, var surrounding: Surrounding? = null) {
    companion object {
        @JvmStatic
        fun parse(xIndex: Int, yIndex: Int, value: Char): Rock {
            return Rock(xIndex, yIndex, RockType.of(value))
        }
    }

    fun roll(direction: TiltDirection, xMax: Int, yMax: Int): Rock {
        if(surrounding == null || type == RockType.SQUARE) return this

        return when(direction) {
            TiltDirection.NORTH -> {
                if(y > 0 && surrounding!!.top == null) {
                    Rock(x, y - 1, type)
                } else {
                    this
                }
            }
            TiltDirection.WEST -> {
                if(x > 0 && surrounding!!.left == null) {
                    Rock(x - 1, y, type)
                } else {
                    this
                }
            }
            TiltDirection.SOUTH -> {
                if(y < yMax && surrounding!!.bottom == null) {
                    Rock(x, y + 1, type)
                } else {
                    this
                }
            }
            TiltDirection.EAST -> {
                if(x < xMax && surrounding!!.right == null) {
                    Rock(x + 1, y, type)
                } else {
                    this
                }
            }
        }
    }

    fun setSurrounding(rocks: Map<Location, Rock>) {
        val tLocation = Location(x, y - 1)
        val bLocation = Location(x, y + 1)
        val lLocation = Location(x - 1, y)
        val rLocation = Location(x + 1, y)

        val top = rocks[tLocation]
        val bottom = rocks[bLocation]
        val left = rocks[lLocation]
        val right = rocks[rLocation]

        this.surrounding = Surrounding(top?.type, bottom?.type, left?.type, right?.type)
    }
}

data class Platform(var lookup: Map<Location, Rock>, var rocks: List<Rock>, var xMax: Int = 0, var yMax: Int = 0) {
    companion object {
        @JvmStatic
        fun parse(rows: List<String>): Platform {
            val lookup = mutableMapOf<Location, Rock>()
            val rocks = rows.first.indices.flatMap { xIndex ->
                rows.mapIndexedNotNull { yIndex, row ->
                    if(row[xIndex] in listOf('#', 'O')) {
                        val location = Location(xIndex, yIndex)
                        val rock = Rock.parse(xIndex, yIndex, row[xIndex])

                        lookup[location] = rock

                        rock
                    } else {
                        null
                    }
                }
            }

            rocks.onEach { it.setSurrounding(lookup) }

            return Platform(lookup, rocks)
        }
    }

    init {
        xMax = rocks.maxOf { it.x }
        yMax = rocks.maxOf { it.y }
    }

    fun tilt(direction: TiltDirection): Platform {
        val signature = Pair(lookup.keys, direction)

        if(signature in TILT_CACHE) {
            return TILT_CACHE[signature] !!
        }

        val cache = lookup.toMutableMap()
        var columns = rocks.groupBy { it.x }

        do {
            cache.clear()

            val previous = columns

            columns = previous
                .map { (yIndex, column) ->
                    val rolled = column.map {
                        val rock = it.roll(direction, xMax, yMax)

                        cache[Location(rock.x, rock.y)] = rock

                        rock
                    }

                    Pair(yIndex, rolled)
                }
                .toMap()

            columns.values.onEach { column ->
                column.onEach { rock ->
                    rock.setSurrounding(cache)
                }
            }
        } while (columns != previous)


        val result = Platform(
            lookup = cache,
            rocks = columns.values.flatten()
        )

        TILT_CACHE[signature] = result

        return result
    }

    fun load(): Int {
        val rows = rocks.groupBy { it.y }

        return rows.entries.sumOf {
            (yMax + 1 - it.key) * it.value.count {
                rock -> rock.type == RockType.ROUND
            }
        }
    }
}

object Day14 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(14, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Total load on north support beam (pt.1): $resultPartOne")
            println("Total load on north support beam (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        return Platform.parse(input)
            .tilt(TiltDirection.NORTH)
            .load()
    }

    fun runPartTwo(input: List<String>): Int {
        val cycle = listOf(
            TiltDirection.NORTH,
            TiltDirection.WEST,
            TiltDirection.SOUTH,
            TiltDirection.EAST
        )

        var platform = Platform.parse(input)

        // Repeating pattern
        // 1000th result == 1000000000th result
        (0..999).onEach {
            cycle.onEach { direction ->
                platform = platform.tilt(direction)
            }
        }

        return platform.load()
    }
}