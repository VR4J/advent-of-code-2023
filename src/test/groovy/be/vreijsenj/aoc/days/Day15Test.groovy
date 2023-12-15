package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day15Test extends Specification {

    def "returns the sum of the hashing results"() {
        given: "the initialization sequence"
            def input = [
                    "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
            ]

        when: "the hashing algorithm is ran"
            def result = new Day15().runPartOne(input)

        then: "the result matches the example answer"
            result == 1320
    }

    def "returns the focusing power of the lens configuration"() {
        given: "the initialization sequence"
            def input = [
                    "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
            ]

        when: "the hashing algorithm is ran"
            def result = new Day15().runPartTwo(input)

        then: "the result matches the example answer"
            result == 145
    }
}
