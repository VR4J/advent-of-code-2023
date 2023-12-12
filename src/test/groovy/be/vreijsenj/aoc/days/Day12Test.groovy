package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day12Test extends Specification {

    def "returns the sum of potential different arrangements"() {
        given: "the damaged condition records"
            def input = [
                    "???.### 1,1,3",
                    ".??..??...?##. 1,1,3",
                    "?#?#?#?#?#?#?#? 1,3,1,6",
                    "????.#...#... 4,1,1",
                    "????.######..#####. 1,6,5",
                    "?###???????? 3,2,1"
            ]

        when: "the arrangements are calculated"
            def result = new Day12().runPartOne(input)

        then: "the result matches the example answer"
            result == 21
    }

    def "returns the sum of potential different arrangements after unfolding"() {
        given: "the damaged condition records"
        def input = [
                "???.### 1,1,3",
                ".??..??...?##. 1,1,3",
                "?#?#?#?#?#?#?#? 1,3,1,6",
                "????.#...#... 4,1,1",
                "????.######..#####. 1,6,5",
                "?###???????? 3,2,1"
        ]

        when: "the arrangements are calculated"
        def result = new Day12().runPartTwo(input)

        then: "the result matches the example answer"
        result == 525152
    }
}
