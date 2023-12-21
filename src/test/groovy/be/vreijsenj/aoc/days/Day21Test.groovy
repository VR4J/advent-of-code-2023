package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day21Test extends Specification {

    def "returns the amount of plots that are reachable after number of steps taken"() {
        given: "the garden map"
            def input = [
                    "...........",
                    ".....###.#.",
                    ".###.##..#.",
                    "..#.#...#..",
                    "....#.#....",
                    ".##..S####.",
                    ".##..#...#.",
                    ".......##..",
                    ".##.#.####.",
                    ".##..##.##.",
                    "..........."
            ]

        when: "the steps are calculated"
            def result = new Day21().runPartOne(input, 6)

        then: "the result matches the example answer"
            result == 16
    }

    def "returns the amount of plots that are reachable after several number of steps taken"() {
        given: "the garden map"
        def input = [
                "...........",
                ".....###.#.",
                ".###.##..#.",
                "..#.#...#..",
                "....#.#....",
                ".##..S####.",
                ".##..#...#.",
                ".......##..",
                ".##.#.####.",
                ".##..##.##.",
                "..........."
        ]

        when: "the steps are calculated"
        def result1 = new Day21().runPartOne(input, 6)
        def result2 = new Day21().runPartOne(input, 10)
        def result3 = new Day21().runPartOne(input, 50)
        def result4 = new Day21().runPartOne(input, 100)

        then: "the result matches the example answer"
            result1 == 16
            result2 == 50
            result3 == 1594
            result4 == 6536
    }
}
