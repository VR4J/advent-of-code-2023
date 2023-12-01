package be.vreijsen.aoc.days

import be.vreijsen.aoc.utils.PuzzleUtils

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

fun main(args: Array<String>) {
    val input = PuzzleUtils.getInput(1, 1)

    val firstResult = runPartOne(input);
    val secondResult = runPartTwo(input);

    println("Sum of all calibration values pt.1: $firstResult")
    println("Sum of all calibration values pt.2: $secondResult")
}

fun runPartOne(input: List<String>): Int {
    val values = input.stream()
            .map { it.filter { char -> char.isDigit() } }
            .map { "${it.first()}" + "${it.last()}" }
            .map { it.toInt() }
            .toList()

    return values.sum()
}

fun runPartTwo(input: List<String>): Int {
    val values = input.stream()
            .map {
                digitsByWord.entries.fold(it) {
                    txt, (key, value) -> txt.replace(key, value)
                }
            }
            .map { it.filter { char -> char.isDigit() } }
            .map { "${it.first()}" + "${it.last()}" }
            .map { it.toInt() }
            .toList()

    return values.sum()
}
