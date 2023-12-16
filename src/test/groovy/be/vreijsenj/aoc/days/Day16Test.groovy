package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day16Test extends Specification {

    def "returns the amount of energized tiles"() {
        given: "the contraption input"
            def input = [
                    ".|...\\....",
                    "|.-.\\.....",
                    ".....|-...",
                    "........|.",
                    "..........",
                    ".........\\",
                    "..../.\\\\..",
                    ".-.-/..|..",
                    ".|....-|.\\",
                    "..//.|....",
            ]

        when: "the beam trajectory is calculated"
            def result = new Day16().runPartOne(input)

        then: "the result matches the example answer"
            result == 46
    }

    def "returns the maximum amount of energized tiles"() {
        given: "the contraption input"
            def input = [
                    ".|...\\....",
                    "|.-.\\.....",
                    ".....|-...",
                    "........|.",
                    "..........",
                    ".........\\",
                    "..../.\\\\..",
                    ".-.-/..|..",
                    ".|....-|.\\",
                    "..//.|....",
            ]

        when: "the energized tiles are calculated"
            def result = new Day16().runPartTwo(input)

        then: "the result matches the example answer"
            result == 51
    }
}
