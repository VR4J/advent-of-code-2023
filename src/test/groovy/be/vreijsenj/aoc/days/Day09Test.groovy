package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day09Test extends Specification {

    def "returns the next extrapolated value for each history trend"() {
        given: "an oasis report"
            def input = [
                    "0 3 6 9 12 15",
                    "1 3 6 10 15 21",
                    "10 13 16 21 30 45",
            ]

        when: "the predicted values are calculated"
            def result = new Day09().runPartOne(input)

        then: "the result matches the example answer"
            result == 114
    }

    def "returns the previous extrapolated value for each history trend"() {
        given: "an oasis report"
        def input = [
                "0 3 6 9 12 15",
                "1 3 6 10 15 21",
                "10 13 16 21 30 45",
        ]

        when: "the predicted values are calculated"
        def result = new Day09().runPartTwo(input)

        then: "the result matches the example answer"
        result == 2
    }
}
