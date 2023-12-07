package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils

import spock.lang.Specification

class Day07Test extends Specification {

     def "returns example total winnings of all hands considering jokers"() {
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
            result == 5905
    }
    
    def "returns total winnings of all hands considering jokers"() {
        given: "the camel card hands"
            def input = PuzzleUtils.getInput(7, 1)

        when: "the games are played"
            def result = new Day07().runPartOne(input)

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
                    "34781 100"
               ]

          when: "the games are played"
               def result = input
                    .map { Hand.parse(it) }
                    .map { it.result() }

          then: "the result"
               result == []
               
     }
}
