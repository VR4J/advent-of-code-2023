package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

data class Sequence(val values: MutableList<Long>) {
    companion object {
        @JvmStatic
        fun parse(line: String, reversed: Boolean): Sequence {
            val values = line.split(" ").map { it.toLong() }.toMutableList()

            return when (reversed) {
                true -> Sequence(values = values.asReversed())
                false -> Sequence(values = values)
            }
        }
    }

    fun isDone(): Boolean {
        return values.all { it == 0L }
    }
}
data class Trend(val values: Sequence, var sequences: MutableList<Sequence> = mutableListOf(values)) {

    companion object {
        @JvmStatic
        fun parse(line: String, reversed: Boolean = false): Trend {
            return Trend(values = Sequence.parse(line, reversed))
        }
    }

    init {
        sequences = getLowerSequences()
    }

    fun predict(): Long {
        sequences.last.values.add(0)

        sequences.asReversed()
                 .zipWithNext { current, next ->
                     next.values.add(next.values.last + current.values.last)
                 }

        return sequences.first.values.last
    }

    private fun getLowerSequences(): MutableList<Sequence> {
        sequences.add(
            getNextSequence(sequences.last)
        )

        if(sequences.last.isDone()) {
            return sequences
        }

        return getLowerSequences()
    }

    private fun getNextSequence(sequence: Sequence): Sequence {
        val differences = sequence.values.zipWithNext {
            current, next -> next - current
        }

        return Sequence(values = differences.toMutableList())
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
        return input.map { Trend.parse(it, reversed = true) }
            .sumOf { it.predict() }
    }
}