package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day11Test extends Specification {

    def "returns the sum of shortest paths between galaxies"() {
        given: "the initial galaxy image"
            def input = [
                    "...#......",
                    ".......#..",
                    "#.........",
                    "..........",
                    "......#...",
                    ".#........",
                    ".........#",
                    "..........",
                    ".......#..",
                    "#...#....."
            ]

        when: "the shortest paths are calculated"
            def result = new Day11().runPartOne(input)

        then: "the result matches the example answer"
            result == 374
    }

    def "parses the image input"() {
        given: "the initial galaxy image"
            def input = [
                    "...#......",
                    ".......#..",
                    "#.........",
                    "..........",
                    "......#...",
                    ".#........",
                    ".........#",
                    "..........",
                    ".......#..",
                    "#...#....."
            ]

        when: "the image is parsed"
            def result = Image.parse(input)

        then: "the result reflects the image"
            result.points.size() == 12 * 13
    }
}
