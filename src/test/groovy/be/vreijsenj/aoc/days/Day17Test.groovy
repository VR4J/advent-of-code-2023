package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day17Test extends Specification {

    def "returns shortest path between city blocks"() {
        given: "the city block map"
            def input = [
                    "2413432311323",
                    "3215453535623",
                    "3255245654254",
                    "3446585845452",
                    "4546657867536",
                    "1438598798454",
                    "4457876987766",
                    "3637877979653",
                    "4654967986887",
                    "4564679986453",
                    "1224686865563",
                    "2546548887735",
                    "4322674655533"
            ]

        when: "the shortest path is calculated"
            def result = new Day17().runPartOne(input)

        then: "the result matches the example answer"
            result == 102
    }

    def "returns shortest path between city blocks using ultra crucibles"() {
        given: "the city block map"
            def input = [
                    "2413432311323",
                    "3215453535623",
                    "3255245654254",
                    "3446585845452",
                    "4546657867536",
                    "1438598798454",
                    "4457876987766",
                    "3637877979653",
                    "4654967986887",
                    "4564679986453",
                    "1224686865563",
                    "2546548887735",
                    "4322674655533"
            ]

        when: "the shortest path is calculated"
            def result = new Day17().runPartTwo(input)

        then: "the result matches the example answer"
            result == 94
    }

    def "returns shortest path between city blocks using ultra crucibles 2"() {
        given: "the city block map"
        def input = [
                "111111111111",
                "999999999991",
                "999999999991",
                "999999999991",
                "999999999991"
        ]

        when: "the shortest path is calculated"
        def result = new Day17().runPartTwo(input)

        then: "the result matches the example answer"
        result == 71
    }
}
