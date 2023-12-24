package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day24Test extends Specification {

    def "returns the amount of intersections within the test area"() {
        given: "the hail positions and velocity"
            def input = [
                    "19, 13, 30 @ -2,  1, -2",
                    "18, 19, 22 @ -1, -1, -2",
                    "20, 25, 34 @ -2, -2, -4",
                    "12, 31, 28 @ -1, -2, -1",
                    "20, 19, 15 @  1, -5, -3"
            ]

        when: "the trajectories are calculated"
            def result = new Day24().runPartOne(input, 7, 27)

        then: "the result matches the example answer"
            result == 2
    }

    def "returns the position and velocity to hit all hailstones"() {
        given: "the hail positions and velocity"
        def input = [
                "19, 13, 30 @ -2,  1, -2",
                "18, 19, 22 @ -1, -1, -2",
                "20, 25, 34 @ -2, -2, -4",
                "12, 31, 28 @ -1, -2, -1",
                "20, 19, 15 @  1, -5, -3"
        ]

        when: "the trajectories are calculated"
        def result = new Day24().runPartTwo(input)

        then: "the result matches the example answer"
        result == 47
    }
}
