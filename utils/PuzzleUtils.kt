package be.vreijsen.aoc.utils

import java.io.File;

object PuzzleUtils {
    fun getInput(day: Number, part: Number): List<String> {
        val path = String.format("inputs/days/%02d_%02d.txt", day, part)
        return File(path).readLines()
    }
}
