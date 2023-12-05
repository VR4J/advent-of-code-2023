package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import org.codehaus.groovy.runtime.DefaultGroovyMethods.power
import kotlin.time.measureTime

data class ScratchCard(val id: Int, val numbers: List<Int>, val winning: List<Int>, var matches: Int = 0) {
    companion object {
        @JvmStatic
        fun parse(line: String): ScratchCard {
            val (id, content) = line.split(":")
            val (winning, numbers) = content.split("|")

            return ScratchCard(
                id = id.filter { it.isDigit() }.toInt(),
                numbers = numbers.trim().split("""\s+""".toRegex()).map(String::toInt),
                winning = winning.trim().split("""\s+""".toRegex()).map(String::toInt),
            )
        }
    }

    init {
        matches = winning.intersect(numbers).size
    }

    fun worth(): Int {
        return power(2.0, matches).toInt() / 2
    }

    fun copies(original: List<ScratchCard>): List<ScratchCard> {
        return original.filter { it.id in IntRange(id + 1, id + matches) }
    }
}

object Day04 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(4, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of all scratch card points (pt.1): $resultPartOne")
            println("Sum of all scratch cards (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        return input.asSequence()
            .map(ScratchCard::parse)
            .map(ScratchCard::worth)
            .sum()
    }

    fun runPartTwo(input: List<String>): Int {
        val cards = input.map(ScratchCard::parse)

        fun getRecursiveCopies(card: ScratchCard, copies: MutableList<ScratchCard>): List<ScratchCard> {
            val currentCardCopies = card.copies(cards)

            copies += currentCardCopies

            currentCardCopies.forEach {
                getRecursiveCopies(it, copies)
            }

            return copies
        }

        val copies = cards
            .flatMap { getRecursiveCopies(it, mutableListOf()) }
            .count()

        return copies + cards.size
    }
}