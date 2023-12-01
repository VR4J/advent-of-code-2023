package be.vreijsen.aoc.utils

import java.io.File;

object PuzzleUtils {
    fun getInput(day: Number, part: Number): List<String> {
        val path = String.format("inputs/day_%02d/part_$part.txt", day)
        return File(path).readLines()
    }
}
