package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day22Test extends Specification {

    def "returns the amount of bricks that can be safely disintegrated"() {
        given: "some input"
            def input = [
                    "1,0,1~1,2,1",
                    "0,0,2~2,0,2",
                    "0,2,3~2,2,3",
                    "0,0,4~0,2,4",
                    "2,0,5~2,2,5",
                    "0,1,6~2,1,6",
                    "1,1,8~1,1,9"
            ]

        when: "a calculation happens"
            def result = new Day22().runPartOne(input)

        then: "the result matches the example answer"
            result == 5
    }

    def "returns the amount of bricks that will fall when disintegrated"() {
        given: "some input"
            def input = [
                    "1,0,1~1,2,1",
                    "0,0,2~2,0,2",
                    "0,2,3~2,2,3",
                    "0,0,4~0,2,4",
                    "2,0,5~2,2,5",
                    "0,1,6~2,1,6",
                    "1,1,8~1,1,9"
            ]

        when: "a calculation happens"
            def result = new Day22().runPartTwo(input)

        then: "the result matches the example answer"
            result == 7
    }
}
