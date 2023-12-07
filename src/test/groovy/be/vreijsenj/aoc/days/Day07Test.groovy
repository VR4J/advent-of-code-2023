package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils

import spock.lang.Specification

class Day07Test extends Specification {

    def "returns total winnings of all hands"() {
        given: "the camel card hands"
            def input = PuzzleUtils.getInput(7, 1)

        when: "the games are played"
            def result = new Day07().runPartOne(input)

        then: "the result matches the example answer"
            result == 6440
    }
}
