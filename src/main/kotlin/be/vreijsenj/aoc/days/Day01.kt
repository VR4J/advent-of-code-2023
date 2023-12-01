package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils

object Day01 {
    val digitsByWord: Map<String, String> = mapOf(
        Pair("one", "o1e"),
        Pair("two", "t2o"),
        Pair("three", "t3e"),
        Pair("four", "f4r"),
        Pair("five", "f5e"),
        Pair("six", "s6x"),
        Pair("seven", "s7n"),
        Pair("eight", "e8t"),
        Pair("nine", "n9e")
    )

    @JvmStatic
    fun main(args: Array<String>) {
        val input = PuzzleUtils.getInput(1, 1)

        val resultPartOne = runPartOne(input);
        val resultPartTwo = runPartTwo(input);

        println("Sum of all calibration values pt.1: $resultPartOne")
        println("Sum of all calibration values pt.2: $resultPartTwo")
    }

    fun runPartOne(input: List<String>): Int {
        return run(input) { it }
    }

    fun runPartTwo(input: List<String>): Int {
        return run(input) {
            digitsByWord.entries.fold(it) {
              txt, (key, value) -> txt.replace(key, value)
            }
        }
    }

    private fun run(input: List<String>, transform: (String) -> String): Int {
        val values = input.stream()
            .map { transform(it) }
            .map { it.filter { char -> char.isDigit() } }
            .map { "${it.first()}" + "${it.last()}" }
            .map { it.toInt() }
            .toList()

        return values.sum()
    }
}
