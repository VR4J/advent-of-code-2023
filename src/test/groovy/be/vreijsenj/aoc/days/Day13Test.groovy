package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import spock.lang.Specification

class Day13Test extends Specification {

    def "returns the sum of left and upper reflections"() {
        given: "the input patterns"
            def input = [
                    "#.##..##.",
                    "..#.##.#.",
                    "##......#",
                    "##......#",
                    "..#.##.#.",
                    "..##..##.",
                    "#.#.##.#.",
                    "",
                    "#...##..#",
                    "#....#..#",
                    "..##..###",
                    "#####.##.",
                    "#####.##.",
                    "..##..###",
                    "#....#..#"
            ].join("\n")

        when: "the reflections are calculated"
            def result = new Day13().runPartOne(input)

        then: "the result matches the example answer"
            result == 405
    }

    def "returns the sum of left and upper reflections fixing potential smudges"() {
        given: "the input patterns"
            def input = [
                    "#.##..##.",
                    "..#.##.#.",
                    "##......#",
                    "##......#",
                    "..#.##.#.",
                    "..##..##.",
                    "#.#.##.#.",
                    "",
                    "#...##..#",
                    "#....#..#",
                    "..##..###",
                    "#####.##.",
                    "#####.##.",
                    "..##..###",
                    "#....#..#"
            ].join("\n")

        when: "the reflections are calculated"
            def result = new Day13().runPartTwo(input)

        then: "the result matches the example answer"
            result == 400
    }
}
