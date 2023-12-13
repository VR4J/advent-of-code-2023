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

    fun getHorizontalCenters(rows: List<String>): Map<Pair<Int, Int>, String> {
        return getCenters(rows)
    }

    fun getHorizontalReflection(rows: List<String>, centers: Map<Pair<Int, Int>, String>): Pair<Pair<Int, Int>, Int> {
        return getReflection(rows, centers)
    }

    fun getVerticalCenters(columns: List<String>): Map<Pair<Int, Int>, String> {
        return getCenters(columns)
    }

    fun getVerticalReflection(columns: List<String>, centers: Map<Pair<Int, Int>, String>): Pair<Pair<Int, Int>, Int> {
        return getReflection(columns, centers)
    }

    private fun getCenters(rows: List<String>): Map<Pair<Int, Int>, String> {
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

    private fun getReflection(rows: List<String>, centers: Map<Pair<Int, Int>, String>): Pair<Pair<Int, Int>, Int> {
        outer@ for (center in centers.keys)  {
            var next = Pair(center.first - 1, center.second + 1)

            while(next.first >= 0 && next.second < rows.size) {
                if(rows[next.first] != rows[next.second]) {
                    // Reflection not valid
                    continue@outer
                }

                next = Pair(next.first -1, next.second + 1)
            }

            return Pair(center, center.second)
        }

        return Pair(Pair(-1, -1), 0)
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
            .map loop@ {
                val vOriginal = it.getVerticalCenters(it.columns)
                val vPrevious = it.getVerticalReflection(it.columns, vOriginal)

                val hOriginal = it.getHorizontalCenters(it.rows)
                val hPrevious = it.getHorizontalReflection(it.rows, hOriginal)

                if(vPrevious.second > 0) {
                    return@loop vPrevious.second
                }

                return@loop hPrevious.second * 100
            }

        return vCenters.sumOf { it }
    }

    fun runPartTwo(input: String): Int {
        var count = 0
        val reflections = input.split("""\n\n""".toRegex())
            .map { Pattern.parse(it) }
            .map loop@ {
                val vOriginal = it.getVerticalCenters(it.columns)
                val vPrevious = it.getVerticalReflection(it.columns, vOriginal)

                val hOriginal = it.getHorizontalCenters(it.rows)
                val hPrevious = it.getHorizontalReflection(it.rows, hOriginal)

                val previous = if(vPrevious.second > 0) vPrevious.second else hPrevious.second * 100

                it.rows.onEachIndexed { yIndex, rowValue ->
                    val rItems = rowValue.chunked(1)

                    rItems.onEachIndexed { xIndex, value ->
                        val column = it.columns[xIndex].chunked(1).toMutableList()
                        val row = rItems.toMutableList()

                        row[xIndex] = if(value == ".") "#" else "."
                        column[yIndex] = if(value == ".") "#" else "."

                        val columns = it.columns.toMutableList()
                        val rows = it.rows.toMutableList()

                        columns[xIndex] = column.joinToString("")
                        rows[yIndex] = row.joinToString("")

                        val vCurrent = it.getVerticalCenters(columns).toMutableMap()

                        if(vPrevious.second > 0) {
                            vCurrent.remove(vPrevious.first)
                        }

                        val vReflection = it.getVerticalReflection(columns, vCurrent)

                        if(previous != vReflection.second && vReflection.second > 0) {
                            return@loop vReflection.second
                        }

                        val hCurrent = it.getHorizontalCenters(rows).toMutableMap()

                        if(hPrevious.second > 0) {
                            hCurrent.remove(hPrevious.first)
                        }

                        val hReflection = it.getHorizontalReflection(rows, hCurrent)

                        if(previous != hReflection.second * 100 && hReflection.second > 0) {
                           return@loop hReflection.second * 100
                        }
                    }
                }

                count++
                return@loop previous
            }

        println(count)

        return reflections.sumOf { it }
    }
}