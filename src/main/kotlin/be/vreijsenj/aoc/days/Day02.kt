package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.math.max

data class Bag(val red: Int = 0, val green: Int = 0, val blue: Int = 0) { }
data class Set(val red: Int = 0, val green: Int = 0, val blue: Int = 0) {

    companion object {
        @JvmStatic
        fun parse(line: String): Set {
            val cubes = line.split(",")

            fun getColorValue(color: String): Int {
                return cubes.find { it.contains(color) }
                    ?.substringBefore(color)
                    ?.trim()
                    ?.toInt() ?: 0
            }

            return Set(
                red = getColorValue("red"),
                green = getColorValue("green"),
                blue = getColorValue("blue")
            )
        }
    }

    fun power(): Int {
        return red * green * blue
    }

    fun inBagContent(bagContent: Bag): Boolean {
        return bagContent.red >= this.red
                && bagContent.green >= this.green
                && bagContent.blue >= this.blue
    }
}
data class Game(val id: String, val sets: List<Set>) {

    companion object {
        @JvmStatic
        fun parse(line: String): Game {
            val (game, set) = line.split(":")

            return Game(
                id = game.filter { it.isDigit() },
                sets = set.split(";")
                          .map { Set.parse(it) }
                          .toList()
            )
        }
    }

    fun minCubeSet(): Set {
        return sets.reduce {
            current, next -> Set(
                red = max(current.red, next.red),
                green = max(current.green, next.green),
                blue = max(current.blue, next.blue)
            )
        }
    }
}

object Day02 {

    @JvmStatic
    fun main(args: Array<String>) {
        val bag = Bag(red = 12, green = 13, blue = 14)
        val input = PuzzleUtils.getInput(2, 1)

        val resultPartOne = runPartOne(input, bag);
        val resultPartTwo = runPartTwo(input)

        println("Sum of all possible game ids (pt.1): $resultPartOne")
        println("Sum of all minimum cube sets (pt.2): $resultPartTwo")
    }

    fun runPartOne(input: List<String>, bag: Bag): Int {
        return input.asSequence()
            .map(Game::parse)
            .filter { it.sets.all { set -> set.inBagContent(bag) } }
            .map(Game::id)
            .map(String::toInt)
            .sum()
    }

    fun runPartTwo(input: List<String>): Int {
        return input
            .map(Game::parse)
            .map(Game::minCubeSet)
            .map(Set::power)
            .sum()
    }
}