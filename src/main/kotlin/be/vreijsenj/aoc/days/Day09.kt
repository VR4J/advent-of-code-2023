package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

data class Trend(val values: MutableList<Long>) {
    companion object {
        @JvmStatic
        fun parse(line: String): Trend {
            return Trend(values = line.split(" ")
                                      .map { it.toLong() }
                                      .toMutableList())
        }
    }

    fun predict(reversed: Boolean = false): Long {
        if(reversed) {
            values.reverse()
        }

        val sequences = getLowerSequences()

        sequences.last.add(0)

        sequences.asReversed()
                 .zipWithNext { current, next ->
                     next.add(next.last + current.last)
                 }

        return sequences.first.last
    }

    private fun getLowerSequences(): MutableList<MutableList<Long>> {
        val sequences = mutableListOf(values)

        while(sequences.last.any { it != 0L }) {
            sequences.add(
                getNextSequence(sequences.last)
            )
        }

        return sequences
    }

    private fun getNextSequence(sequence: MutableList<Long>): MutableList<Long> {
        return sequence.zipWithNext { current, next -> next - current }.toMutableList()
    }
}
object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(9, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of extrapolated values (pt.1): $resultPartOne")
            println("Sum of extrapolated values (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Long {
        return input.map { Trend.parse(it) }
                    .sumOf { it.predict() }
    }

    fun runPartTwo(input: List<String>): Long {
        return input.map { Trend.parse(it) }
            .sumOf { it.predict(reversed = true) }
    }
}