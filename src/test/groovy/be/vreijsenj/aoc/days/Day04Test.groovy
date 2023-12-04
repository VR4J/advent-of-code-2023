package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day04Test extends Specification {

    def "returns the points of all scratch cards"() {
        given: "the scratch cards"
            def input = [
                    "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                    "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                    "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                    "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                    "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                    "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
            ]

        when: "the cards are checked"
            def result = new Day04().runPartOne(input)

        then: "the points matches the example answer"
            result == 13
    }

    def "returns the amount of scratch cards"() {
        given: "the scratch cards"
            def input = [
                    "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                    "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                    "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                    "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                    "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                    "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
            ]

        when: "the cards are checked"
            def result = new Day04().runPartTwo(input)

        then: "the points matches the example answer"
            result == 30
    }

    def "parses the scratch card"() {
        given: "a scratch card"
            def input = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"

        when: "the cards is parsed"
            def result = ScratchCard.parse(input)

        then: "the points matches the example answer"
            result instanceof ScratchCard

            result.id == 1
            result.matches == 4
            result.numbers == [83, 86, 6, 31, 17, 9, 48, 53]
            result.winning == [41, 48, 83, 86, 17]
    }
}
