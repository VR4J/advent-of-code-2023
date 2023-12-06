package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

data class BoatRace(val time: LongRange, val record: Long) {
    companion object {
        @JvmStatic
        fun parse(time: String, distance: String): BoatRace {
            return BoatRace(0..time.toLong(), distance.toLong())
        }
    }

    fun getWaysToWin(): Int {
        return time.map { getTravelDistance(it) }
                   .count { it > record }
    }

    private fun getTravelDistance(holding: Long): Long {
        return holding * (time.last - holding)
    }
}

object Day06 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val inputPartOne = PuzzleUtils.getInput(6, 1)
            val resultPartOne = runPartOne(inputPartOne)

            val inputPartTwo = PuzzleUtils.getInput(6, 2)
            val resultPartTwo = runPartTwo(inputPartTwo)

            println("Ways to beat the records (pt.1): $resultPartOne")
            println("Ways to beat the records (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val times = input.first.split("""\s+""".toRegex())
            .filter { it.isNumeric() }

        val distances = input.last.split("""\s+""".toRegex())
            .filter { it.isNumeric() }

        return times.zip(distances)
            .map { BoatRace.parse(it.first, it.second) }
            .map { it.getWaysToWin() }
            .reduce(Int::times)
    }

    fun runPartTwo(input: List<String>): Int {
        val (_, time) = input.first.split(":").map { it.trim() }
        val (_, distance) = input.last.split(":").map { it.trim() }

        val race = BoatRace.parse(time, distance)

        return race.getWaysToWin()
    }

    private fun String.isNumeric(): Boolean {
        return toDoubleOrNull() != null
    }
}