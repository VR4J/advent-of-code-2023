package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day06Test extends Specification {

    def "returns number of ways to beat the current records"() {
        given: "the race times and records"
            def input = [
                    "Time:      7  15   30",
                    "Distance:  9  40  200"
            ]


        when: "the ways to beat the records are determined"
            def result = new Day06().runPartOne(input)

        then: "result matches the example answer"
            result == 288
    }

    def "returns number of ways to beat the current records"() {
        given: "the race times and records"
        def input = [
                "Time:      71530",
                "Distance:  940200"
        ]

        when: "the ways to beat the records are determined"
        def result = new Day06().runPartTwo(input)

        then: "result matches the example answer"
        result == 71503
    }
}
