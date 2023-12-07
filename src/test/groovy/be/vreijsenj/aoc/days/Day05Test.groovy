package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day05Test extends Specification {

    def "returns closest location that needs a seed"() {
        given: "the almanac input"
            def input = """
                seeds: 79 14 55 13

                seed-to-soil map:
                50 98 2
                52 50 48
                
                soil-to-fertilizer map:
                0 15 37
                37 52 2
                39 0 15
                
                fertilizer-to-water map:
                49 53 8
                0 11 42
                42 0 7
                57 7 4
                
                water-to-light map:
                88 18 7
                18 25 70
                
                light-to-temperature map:
                45 77 23
                81 45 19
                68 64 13
                
                temperature-to-humidity map:
                0 69 1
                1 0 69
                
                humidity-to-location map:
                60 56 37
                56 93 4
            """.trim()

        when: "the almanac is read"
            def result = new Day05().runPartOne(input)

        then: "the closest seed matches the example answer"
            result == 35
    }

    def "returns closest location that needs a seed considering seed ranges"() {
        given: "the almanac input"
        def input = """
                seeds: 79 14 55 13

                seed-to-soil map:
                50 98 2
                52 50 48
                
                soil-to-fertilizer map:
                0 15 37
                37 52 2
                39 0 15
                
                fertilizer-to-water map:
                49 53 8
                0 11 42
                42 0 7
                57 7 4
                
                water-to-light map:
                88 18 7
                18 25 70
                
                light-to-temperature map:
                45 77 23
                81 45 19
                68 64 13
                
                temperature-to-humidity map:
                0 69 1
                1 0 69
                
                humidity-to-location map:
                60 56 37
                56 93 4
            """.trim()

        when: "the almanac is read"
            def result = new Day05().runPartTwo(input)

        then: "the closest seed matches the example answer"
            result == 46
    }
}
