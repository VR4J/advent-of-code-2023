package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day07Test extends Specification {

    def "returns total winnings of all hands"() {
        given: "the camel card hands"
            def input = [
                    "32T3K 765",
                    "T55J5 684",
                    "KK677 28",
                    "KTJJT 220",
                    "QQQJA 483",
            ]

        when: "the games are played"
            def result = new Day07().runPartOne(input)

        then: "the result matches the example answer"
            result == 6440
    }
}

