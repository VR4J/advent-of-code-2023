package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import java.lang.RuntimeException
import kotlin.time.measureTime
data class Hand(
    val cards: List<CamelCard>,
    val bid: Int
): Comparable<Hand> {
    companion object {
        @JvmStatic
        fun parse(line: String): Hand {
            val (cards, bid) = line.split(" ")

            return Hand(
                cards = cards.chunked(1).map { CamelCard.parse(it) },
                bid = bid.toInt()
            )
        }
    }

    fun result(): Int {
        val group = cards.groupingBy { it.value }
                         .eachCount()

        if(group.any { it.value == group.size }) {
            // Five of a kind
            return 7
        }

        if(group.any { it.value == group.size - 1 }) {
            // Four of a kind
            return 6
        }

        if(group.containsValue(3) && group.containsValue(2)) {
            // Full house
            return 5
        }

        if(group.containsValue(3) && ! group.containsValue(2)) {
            // Thee of a kind
            return 4
        }

        if(group.count { it.value == 2 } == 2) {
            // Two pair
            return 3
        }

        if(group.count { it.value == 2} == 1) {
            // One pair
            return 2
        }

        if(group.all { it.value == 1}) {
            // high card
            return 1
        }

        throw RuntimeException("Could not determine hand result for cards: ${this.cards}")
    }

    override fun compareTo(other: Hand): Int {
        if(this.result() > other.result()) {
            return 1
        }

        if(this.result() == other.result()) {
            val pair = this.cards.zip(other.cards)
                .filter { it.first != it.second }
                .first

            if(pair.first.value > pair.second.value) {
                return 1
            }

            if(pair.first.value < pair.second.value) {
                return -1
            }

            throw RuntimeException("Unable to compare Hand results")
        }

        if(this.result() < other.result()) {
            return -1
        }

        throw RuntimeException("Unable to compare Hand results")
    }
}
data class CamelCard(val name: String, val value: Int) {
    companion object {
        @JvmStatic
        fun parse(name: String): CamelCard {
            return CamelCard(
                name = name,
                value = getValue(name),
            )
        }

        private fun getValue(name: String): Int {
            return when(name) {
                "A" -> 14
                "K" -> 13
                "Q" -> 12
                "T" -> 10
                "J" -> 1
                else -> name.toInt()
            }
        }
    }
}
object Day07 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val inputPartOne = PuzzleUtils.getInput(7, 1)
            val resultPartOne = runPartOne(inputPartOne)

            println("Ways to beat the records (pt.1): $resultPartOne")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        return input
            .map { Hand.parse(it) }
            .sorted()
            .mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
            .sum()
    }
}