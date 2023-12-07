package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import java.lang.RuntimeException
import kotlin.time.measureTime

enum class ResultType(val strength: Int, val isPresent: (occurrences: Map<Int, Int>, jokers: Int) -> Boolean) {
    FIVE_OF_A_KIND(7, { occurrences, jokers ->
        when(jokers) {
            0, 1, 2, 3 -> occurrences.any { it.value == (5 - jokers) }
            else -> true
        }
    }),
    FOUR_OF_A_KIND(6, { occurrences, jokers ->
        when(jokers) {
            0, 1, 2 -> occurrences.any { it.value == (4 - jokers) }
            else -> true
        }
    }),
    FULL_HOUSE(5, { occurrences, jokers ->
        when (jokers) {
            0 -> occurrences.containsValue(3) && occurrences.containsValue(2)
            1 -> {
                occurrences.count { it.value == 2 } == 2
                        || (occurrences.count { it.value == 3 } == 1 && occurrences.count { it.value == 1 } == 1)
            }
            2 -> {
                occurrences.count { it.value == 2 } == 1
                        && occurrences.count {it.value == 1 } == 1
            }
            else -> {
                // with three or more jokers you will always have four of a kind, which is higher
                false
            }
        }
    }),
    THREE_OF_A_KIND(4, { occurrences, jokers ->
        when(jokers) {
            0, 1 -> occurrences.any { it.value == (3 - jokers) }
            else -> true
        }
    }),
    TWO_PAIRS(3, { occurrences, jokers ->
        when(jokers) {
            0 -> occurrences.count { it.value == 2 } == 2
            else -> {
                // With one or more jokers you will already have three of a kind, which is higher
                false
            }
        }
    }),
    ONE_PAIR(2, { occurrences, jokers ->
        when(jokers) {
            0 -> occurrences.count { it.value == 2 } == 1
            1 -> true
            else -> {
                // With two or more jokers you will always have three of a kind, which is higher
                false
            }
        }
    }),
    HIGHEST_CARD(1, { _,_ -> true })
}

data class Hand(
    val cards: List<CamelCard>,
    val jokers: List<CamelCard> = cards.filter { it.value == 1 },
    var result: ResultType = ResultType.HIGHEST_CARD,
    val bid: Int
): Comparable<Hand> {
    companion object {
        @JvmStatic
        fun parse(line: String, getCardValue: (String) -> Int): Hand {
            val (cards, bid) = line.split(" ")

            return Hand(
                cards = cards.chunked(1).map { CamelCard.parse(it, getCardValue) },
                bid = bid.toInt()
            )
        }
    }

    init {
        result = result()
    }

    override fun compareTo(other: Hand): Int {
        if(this.result.strength > other.result.strength) {
            return 1
        }

        if(this.result.strength == other.result.strength) {
            val pair = this.cards.zip(other.cards)
                .filter { it.first != it.second }
                .first

            if(pair.first.value > pair.second.value) {
                return 1
            }

            if(pair.first.value < pair.second.value) {
                return -1
            }
        }

        if(this.result.strength < other.result.strength) {
            return -1
        }

        throw RuntimeException("Unable to determine winning hand.")
    }

    private fun result(): ResultType {
        val grouped = cards
            .filter { it.value > 1 }
            .groupingBy { it.value }
            .eachCount()

        return ResultType.entries.toTypedArray()
            .filter { it.isPresent(grouped, jokers.size) }
            .first
    }
}
data class CamelCard(val name: String, val value: Int) {
    companion object {
        @JvmStatic
        fun parse(name: String, getCardValue: (String) -> Int): CamelCard {
            return CamelCard(
                name = name,
                value = getCardValue(name),
            )
        }
    }
}
object Day07 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(7, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Total winnings (pt.1): $resultPartOne")
            println("Total winnings (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val getCardValue = { name: String ->
            when(name) {
                "A" -> 14
                "K" -> 13
                "Q" -> 12
                "J" -> 11
                "T" -> 10
                else -> name.toInt()
            }
        }

        return run(input, getCardValue)
    }

    fun runPartTwo(input: List<String>): Int {
        val getCardValue = { name: String ->
            when(name) {
                "A" -> 14
                "K" -> 13
                "Q" -> 12
                "T" -> 10
                "J" -> 1
                else -> name.toInt()
            }
        }

        return run(input, getCardValue)
    }

    private fun run(input: List<String>, getCardValue: (String) -> Int): Int {
        return input
            .map { Hand.parse(it, getCardValue) }
            .sorted()
            .mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
            .sum()
    }
}
