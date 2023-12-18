package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day18Test extends Specification {

    def "returns the amount of cubic meters of lava that can be held"() {
        given: "the dig plan"
            def input = [
                    "R 6 (#70c710)",
                    "D 5 (#0dc571)",
                    "L 2 (#5713f0)",
                    "D 2 (#d2c081)",
                    "R 2 (#59c680)",
                    "D 2 (#411b91)",
                    "L 5 (#8ceee2)",
                    "U 2 (#caa173)",
                    "L 1 (#1b58a2)",
                    "U 2 (#caa171)",
                    "R 2 (#7807d2)",
                    "U 3 (#a77fa3)",
                    "L 2 (#015232)",
                    "U 2 (#7a21e3)",
            ]

        when: "the drill goes brrrr"
            def result = new Day18().runPartOne(input)

        then: "the result matches the example answer"
            result == 62
    }

    def "returns the amount of cubic meters of lava that can be held from hex"() {
        given: "the dig plan"
            def input = [
                    "R 6 (#70c710)",
                    "D 5 (#0dc571)",
                    "L 2 (#5713f0)",
                    "D 2 (#d2c081)",
                    "R 2 (#59c680)",
                    "D 2 (#411b91)",
                    "L 5 (#8ceee2)",
                    "U 2 (#caa173)",
                    "L 1 (#1b58a2)",
                    "U 2 (#caa171)",
                    "R 2 (#7807d2)",
                    "U 3 (#a77fa3)",
                    "L 2 (#015232)",
                    "U 2 (#7a21e3)",
            ]

        when: "the drill goes brrrr"
            def result = new Day18().runPartTwo(input)

        then: "the result matches the example answer"
            result == 952408144115
    }

    def "reads the instructions from hex value"() {
        given: "some hexadecimal dig instructions"
            def start = new Hole(0, 0)
            def input = [
                    "R 6 #70c710",
                    "D 5 #0dc571",
                    "L 2 #5713f0",
                    "D 2 #d2c081",
                    "R 2 #59c680"
            ]

        when: "the instructions are parsed"
            def result = input.collect { ColorEdge.parseFromHex(it, start) }

        then: "the length and directions are clear"
            result[0].start == new Hole(0, 0)
            result[0].end == new Hole(461937, 0)

            result[1].start == new Hole(0, 0)
            result[1].end == new Hole(0, 56407)

            result[2].start == new Hole(0, 0)
            result[2].end == new Hole(356671, 0)

            result[3].start == new Hole(0, 0)
            result[3].end == new Hole(0, 863240)

            result[4].start == new Hole(0, 0)
            result[4].end == new Hole(367720, 0)

    }
}
