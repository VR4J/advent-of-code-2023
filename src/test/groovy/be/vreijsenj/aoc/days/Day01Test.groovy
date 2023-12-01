package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day01Test extends Specification {

    def "returns sum of all calibration values, considering digits only"() {
        given: "the example puzzle input"
            def input = [
                    "1abc2",
                    "pqr3stu8vwx",
                    "a1b2c3d4e5f",
                    "treb7uchet"
            ]

        when: "the calibration values are calculated"
            def result = new Day01().runPartOne(input)

        then: "the result matches the example answer"
            result == 142
    }

    def "returns sum of all calibration values, considering both digits and spelled out digits"() {
        given: "the example puzzle input"
            def input = [
                    "two1nine",
                    "eightwothree",
                    "abcone2threexyz",
                    "xtwone3four",
                    "4nineeightseven2",
                    "zoneight234",
                    "7pqrstsixteen"
            ]

        when: "the calibration values are calculated"
            def result = new Day01().runPartTwo(input)

        then: "the result matches the example answer"
            result == 281
    }
}
