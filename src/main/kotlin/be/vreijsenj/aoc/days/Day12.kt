package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

val CACHE = mutableMapOf<Triple<Int, Int, Int>, Long>()

data class ConditionRecord(val springs: List<String>, val groups: List<Int>) {
    companion object {

        @JvmStatic
        fun parse(line: String, times: Int): ConditionRecord {
            val (springs, groups) = line.split(" ")

            return ConditionRecord(
                springs = unfold(springs, times, "?").chunked(1),
                groups = unfold(groups, times, ",").split(",").map { it.toInt() }
            )
        }

        private fun unfold(line: String, times: Int, separator: String): String {
            return (0..< times).fold(line) {
                    acc, _ -> acc + separator + line
            }
        }
    }

    fun arrangements(): Long {
        CACHE.clear()
        return group(0, 0, 0)
    }

    private fun group(currentPositionIndex: Int, currentGroupIndex: Int, currentGroupLength: Int): Long {
        val signature = Triple(currentPositionIndex, currentGroupIndex, currentGroupLength)

        if(signature in CACHE) {
            return CACHE[signature]!!
        }

        // Check whether we are out of bounce, which means we reached the end.
        if(currentPositionIndex == springs.size) {

            // Check whether we just finished a group, and were preparing for the next
            if(currentGroupIndex == groups.size && currentGroupLength == 0) {
                return 1
            }

            // Check whether we could have closed off the last group
            if(currentGroupIndex == groups.size - 1 && currentGroupLength == groups.last) {
                return 1
            }

            return 0
        }

        var count = 0L
        val possibilities = listOf("#", ".")

        possibilities.onEach { possibility ->
            val value = springs[currentPositionIndex]

            if(value == possibility || value == "?") {
                // Prevent closing an "empty" group
                if (possibility == "." && currentGroupLength == 0) {

                    // Unable to create group, continue and try to create group again
                    count += group(currentPositionIndex + 1, currentGroupIndex, 0)
                }

                // Check whether the group is actually valid, giving the current group constraints
                if (possibility == "." && currentGroupLength > 0 && currentGroupIndex < groups.size && groups[currentGroupIndex] == currentGroupLength) {

                    // Close current group, move on to next group
                    count += group(currentPositionIndex + 1, currentGroupIndex + 1, 0)
                }

                if (possibility == "#") {

                    // Continue current group
                    count += group(currentPositionIndex + 1, currentGroupIndex, currentGroupLength + 1)
                }
            }
        }

        CACHE[signature] = count

        return count
    }
}

object Day12 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(12, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of possible arrangements (pt.1): $resultPartOne")
            println("Sum of possible arrangements (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Long {
        return input.map { ConditionRecord.parse(it, 0) }
            .sumOf { it.arrangements() }
    }

    fun runPartTwo(input: List<String>): Long {
        return input.map { ConditionRecord.parse(it, 4) }
            .sumOf { it.arrangements() }
    }
}