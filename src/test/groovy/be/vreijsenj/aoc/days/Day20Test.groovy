package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day20Test extends Specification {

    def "returns"() {
        given: "the input"
            def input = [ ]

        when: "a calculation happens"
            def result = new Day20().runPartOne(input)

        then: "the result matches the example answer"
            result == 0
    }
}
