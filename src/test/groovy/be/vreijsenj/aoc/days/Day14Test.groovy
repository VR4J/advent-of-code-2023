package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day14Test extends Specification {

    def "returns the total load caused by all rocks"() {
        given: "the platform input"
            def input = [
                    "O....#....",
                    "O.OO#....#",
                    ".....##...",
                    "OO.#O....O",
                    ".O.....O#.",
                    "O.#..O.#.#",
                    "..O..#O..O",
                    ".......O..",
                    "#....###..",
                    "#OO..#...."
            ]

        when: "the lever is pulled, and load is calculated"
            def result = new Day14().runPartOne(input)

        then: "the result matches the example answer"
            result == 136
    }

    def "returns the total load caused by all rocks after all cycles"() {
        given: "the platform input"
            def input = [
                    "O....#....",
                    "O.OO#....#",
                    ".....##...",
                    "OO.#O....O",
                    ".O.....O#.",
                    "O.#..O.#.#",
                    "..O..#O..O",
                    ".......O..",
                    "#....###..",
                    "#OO..#...."
            ]

        when: "the lever is pulled, and load is calculated"
            def result = new Day14().runPartTwo(input)

        then: "the result matches the example answer"
            result == 64
    }
}
