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
                "QQQJA 483"
            ]
         
        when: "the games are played"
            def result = new Day07().runPartOne(input)

        then: "the result matches the example answer"
            result == 6440
    }
    
    def "returns total winnings of all hands considering jokers"() {
        given: "the camel card hands"
            def input = [
                    "32T3K 765",
                    "T55J5 684",
                    "KK677 28",
                    "KTJJT 220",
                    "QQQJA 483"
            ]

        when: "the games are played"
            def result = new Day07().runPartTwo(input)

        then: "the result matches the example answer"
            result == 5905
    }

    def "returns the types for different hands"() {
        given: "the camel card hands"
            def input = [
                "JJ333 100",
                "J3332 100",
                "J3322 100",
                "JJ245 100",
                "22445 100",
                "J2589 100",
                "34782 100"
            ]

        when: "the games are played"
            def getCardValue = { name ->
                return switch (name) {
                    case "A" -> 14
                    case "K" -> 13
                    case "Q" -> 12
                    case "T" -> 10
                    case "J" -> 1
                    default -> name.toInteger()
                }
            }

            def result = input
                    .collect { Hand.parse(it, getCardValue) }
                    .collect { it.result }

        then: "the result"
            result == [
                ResultType.FIVE_OF_A_KIND,
                ResultType.FOUR_OF_A_KIND,
                ResultType.FULL_HOUSE,
                ResultType.THREE_OF_A_KIND,
                ResultType.TWO_PAIRS,
                ResultType.ONE_PAIR,
                ResultType.HIGHEST_CARD
            ]
     }
}
