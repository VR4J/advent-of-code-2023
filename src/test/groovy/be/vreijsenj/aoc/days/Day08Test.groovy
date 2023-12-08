package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day08Test extends Specification {

    def "returns the number of steps to reach ZZZ"() {
        given: "the map instructions"
            def input = [
                    "LLR",
                    "",
                    "AAA = (BBB, BBB)",
                    "BBB = (AAA, ZZZ)",
                    "ZZZ = (ZZZ, ZZZ)"
            ]

        when: "the route is calculated"
            def result = new Day08().runPartOne(input)

        then: "the result matches the example answer"
            result == 6
    }

    def "returns the number of simultaneous steps to reach *Z"() {
        given: "the map instructions"
            def input = [
                    "LR",
                    "",
                    "11A = (11B, XXX)",
                    "11B = (XXX, 11Z)",
                    "11Z = (11B, XXX)",
                    "22A = (22B, XXX)",
                    "22B = (22C, 22C)",
                    "22C = (22Z, 22Z)",
                    "22Z = (22B, 22B)",
                    "XXX = (XXX, XXX)"
            ]

        when: "the route is calculated"
            def result = new Day08().runPartTwo(input)

        then: "the result matches the example answer"
            result == 6
    }

    def "returns correct direction for step"() {
        given: "the map instructions"
            def input = [
                    "LLR",
                    "",
                    "AAA = (BBB, BBB)",
                    "BBB = (AAA, ZZZ)",
                    "ZZZ = (ZZZ, ZZZ)"
            ]

        when: "the network is constructed"
            def network = Network.parse(input)

        then: "the step is calculated correctly"
            network.getDirection(1) == Direction.LEFT
            network.getDirection(2) == Direction.LEFT
            network.getDirection(3) == Direction.RIGHT

            network.getDirection(16) == Direction.LEFT
            network.getDirection(17) == Direction.LEFT
            network.getDirection(18) == Direction.RIGHT
    }
}
