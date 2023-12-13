package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime
data class Pattern(val rows: List<String>, val columns: List<String>) {
    companion object {

        @JvmStatic
        fun parse(input: String): Pattern {
            val rows = input.lines()
            val columns = rows.first.indices.map { xIndex ->
                rows.map { it[xIndex] }.joinToString("")
            }

            return Pattern(rows, columns)
        }
    }

    fun getHorizontalReflection(centers: Map<Pair<Int, Int>, String>): Int {
        var total = 0

        outer@ for (center in centers.keys)  {
            var next = Pair(center.first - 1, center.second + 1)

            while(next.first >= 0 && next.second < rows.size) {
                if(rows[next.first] != rows[next.second]) {
                    // Reflection not valid
                    continue@outer
                }

                next = Pair(next.first - 1, next.second + 1)
            }

            total += center.second
        }

        return total
    }

    fun getHorizontalCenters(): Map<Pair<Int, Int>, String> {
        val centers = rows.zipWithNext()
            .mapIndexedNotNull { index, (current, next) ->
                if(current == next) {
                    Pair(Pair(index, index + 1), current)
                } else {
                    null
                }
            }
            .toMap()


        return centers
    }

    fun getVerticalCenters(): Map<Pair<Int, Int>, String> {
        val centers = columns.zipWithNext()
            .mapIndexedNotNull { index, (current, next) ->
                if(current == next) {
                    Pair(Pair(index, index + 1), current)
                } else {
                    null
                }
            }
            .toMap()


        return centers
    }

    fun getVerticalReflection(centers: Map<Pair<Int, Int>, String>): Int {
        var total = 0

        outer@ for (center in centers.keys)  {
            var next = Pair(center.first - 1, center.second + 1)

            while(next.first >= 0 && next.second < columns.size) {
                if(columns[next.first] != columns[next.second]) {
                    // Reflection not valid
                    continue@outer
                }

                next = Pair(next.first -1, next.second + 1)
            }

            total += center.second
        }

        return total
    }
}

object Day13 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInputAsText(13, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of vertical and horizontal reflections (pt.1): $resultPartOne")
            println("Sum of vertical and horizontal reflections (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: String): Int {
        val vCenters = input.split("""\n\n""".toRegex())
            .map { Pattern.parse(it) }
            .map {
                val centers = it.getVerticalCenters()
                it.getVerticalReflection(centers)
            }


        val hCenters = input.split("""\n\n""".toRegex())
            .map { Pattern.parse(it) }
            .map {
                val centers = it.getHorizontalCenters()
                it.getHorizontalReflection(centers)
            }


        return hCenters.sumOf { it * 100 } + vCenters.sum()
    }

    fun runPartTwo(input: String): Long {
        return 0L
    }
}