package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import java.lang.RuntimeException
import kotlin.time.measureTime

enum class Type(val strength: Int, val isPresent: (occurrences: Map<Int, Int>, jokers: Int) -> Boolean) {
    FIVE_OF_A_KIND(7, { occurrences, jokers ->
        if(jokers >= 4) {
            true
        } else {
            occurrences.any { it.value == (5 - jokers) }
        }
    }),
    FOUR_OF_A_KIND(6, { occurrences, jokers ->
        if(jokers >= 3) {
            true
        } else {
            occurrences.any { it.value == (4 - jokers) }
        }
    }),
    FULL_HOUSE(5, { occurrences, jokers ->
        if(jokers == 0) {
            occurrences.containsValue(3) && occurrences.containsValue(2)
        } else if(jokers == 1) {
            occurrences.count { it.value == 2 } == 2
                || (occurrences.count { it.value == 3 } == 1 && occurrences.count { it.value == 1 } == 1)
        } else if(jokers == 2) {
            occurrences.count { it.value == 2 } == 1
                && occurrences.count {it.value == 1 } == 1
        } else {
            // with three or more jokers you will always have four of a kind.
            false
        }
    }),
    THREE_OF_A_KIND(4, { occurrences, jokers ->
        if(jokers >= 2) {
            true
        } else {
            occurrences.any { it.value == (3 - jokers) }
        }
    }),
    TWO_PAIRS(3, { occurrences, jokers ->
        if(jokers == 0) {
            occurrences.count { it.value == 2 } == 2
        } else {
            // with two or more jokers you will always have three of a kind 
            false
        }
    }),
    ONE_PAIR(2, { occurrences, jokers ->
        if(jokers == 0) {
            occurrences.count { it.value == 2 } == 1
        } else { 
            true
        }
    }),
    HIGHEST_CARD(1, { occurrences, jokers ->
        if(jokers > 0) {
            false
        } else {
            true
        }
    })
}

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

        val occurrences = group.filter { it.key != 1 }
        val jokers = group.getOrElse(1) { 0 }
        
        val result = Type.values()
                   .sortedByDescending { it.strength }
                   .filter { it.isPresent(occurrences, jokers) }
                   .first
        
        print(result)

        return result.strength
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
