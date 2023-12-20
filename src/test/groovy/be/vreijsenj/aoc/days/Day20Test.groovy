package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day20Test extends Specification {

    def "returns the number of pulses sent after pushing the button 1000 times"() {
        given: "the input"
            def input = [
                    "broadcaster -> a, b, c",
                    "%a -> b",
                    "%b -> c",
                    "%c -> inv",
                    "&inv -> a"
            ]

        when: "a calculation happens"
            def result = new Day20().runPartOne(input)

        then: "the result matches the example answer"
            result == 32000000
    }

    def "returns another the number of pulses sent after pushing the button 1000 times"() {
        given: "the input"
            def input = [
                    "broadcaster -> a",
                    "%a -> inv, con",
                    "&inv -> b",
                    "%b -> con",
                    "&con -> output"
            ]

        when: "a calculation happens"
            def result = new Day20().runPartOne(input)

        then: "the result matches the example answer"
            result == 11687500
    }
}
