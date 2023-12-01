package be.vreijsenj.aoc.utils

import spock.lang.Specification

class PuzzleUtilsTest extends Specification {

    def "get puzzle input from resources folder"() {
        given: 'the day and part'
        def day = 1
        def part = 1

        when: 'getting the puzzle input'
        def result = PuzzleUtils.getInput(day, part)

        then: 'the result is a list of line representing the file content'
        result == [
                "1abc2",
                "pqr3stu8vwx",
                "a1b2c3d4e5f",
                "treb7uchet"
        ]
    }
}
