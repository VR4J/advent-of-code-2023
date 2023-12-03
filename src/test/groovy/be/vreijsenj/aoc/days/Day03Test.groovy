package be.vreijsenj.aoc.days

import kotlin.ranges.IntRange
import spock.lang.Specification

class Day03Test extends Specification {

    def "returns the sum of part numbers"() {
        given: "the engine schematic"
            def input = [
                    "467..114..",
                    "...*......",
                    "..35..633.",
                    "......#...",
                    "617*......",
                    ".....+.58.",
                    "..592.....",
                    "......755.",
                    "...\$.*....",
                    ".664.598.."
            ]

        when: "the part numbers are identified"
            def result = new Day03().runPartOne(input)

        then: "the sum is equal to the given example"
            result == 4361
    }

    def "returns the sum of all gear ratios"() {
        given: "the engine schematic"
            def input = [
                    "467..114..",
                    "...*......",
                    "..35..633.",
                    "......#...",
                    "617*......",
                    ".....+.58.",
                    "..592.....",
                    "......755.",
                    "...\$.*....",
                    ".664.598.."
            ]

        when: "the gear ratios are identified"
            def result = new Day03().runPartTwo(input)

        then: "the sum is equal to the given example"
            result == 467835
    }

    def "reads numbers from schematic line"() {
        given: "an engine schematic line"
            def line = "467..114.."

        when: "numbers are read"
            def result = EngineSchematicLine.parse(line)

        then: "the numbers and ranges are returned"
            result.numbers.size() == 2

            result.numbers.first.value == 467
            result.numbers.first.location.equals new IntRange(0, 2)

            result.numbers.last.value == 114
            result.numbers.last.location.equals new IntRange(5, 7)
    }

    def "reads symbols from schematic line"() {
        given: "an engine schematic line"
            def line = "...\$.*...."

        when: "numbers are read"
            def result = EngineSchematicLine.parse(line)

        then: "the numbers and ranges are returned"
            result.symbols.size() == 2

            result.symbols.first.value == "\$"
            result.symbols.first.location.equals 3

            result.symbols.last.value == "*"
            result.symbols.last.location.equals 5
    }
}
