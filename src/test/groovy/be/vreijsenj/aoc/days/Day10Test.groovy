package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day10Test extends Specification {

    def "returns the farthest point in a pipe loop given the starting position"() {
        given: "the map of surface pipes"
            def input = [
                "-L|F7",
                "7S-7|",
                "L|7||",
                "-L-J|",
                "L|-JF"
            ]

        when: "the farthest point from start is calculated"
            def result = new Day10().runPartOne(input)

        then: "the result matches the example answer"
            result == 4
    }

    def "returns the farthest point in a pipe loop given the starting position"() {
        given: "the map of surface pipes"
            def input = [
                    "7-F7-",
                    ".FJ|7",
                    "SJLL7",
                    "|F--J",
                    "LJ.LJ"
            ]

        when: "the farthest point from start is calculated"
            def result = new Day10().runPartOne(input)

        then: "the result matches the example answer"
            result == 8
    }

    def "returns the amount of tiles enclosed by the loop"() {
        given: "the map of surface pipes"
            def input = [
                    "...........",
                    ".S-------7.",
                    ".|F-----7|.",
                    ".||.....||.",
                    ".||.....||.",
                    ".|L-7.F-J|.",
                    ".|..|.|..|.",
                    ".L--J.L--J.",
                    "..........."
            ]

        when: "the enclosed tiles are calculated"
            def result = new Day10().runPartTwo(input)

        then: "the result matches the example answer"
            result == 4
    }

    def "calculates the containing points inside the loop"() {
        given: "a loop"
            def input = [
                    "...........",
                    ".S--7......",
                    ".|..|......",
                    ".|..|......",
                    ".|..L---7..",
                    ".|......|..",
                    ".|......|..",
                    ".L------J..",
                    "..........."
            ]

        when: "the edges are calculated"
            def result = new Day10().runPartTwo(input)

        then: "the result matches the example answer"
            result == 18
    }

    def "calculates the edges of the loop"() {
        given: "a loop"
            def input = [
                    "...........",
                    ".S--7......",
                    ".|..|......",
                    ".|..|......",
                    ".|..L---7..",
                    ".|......|..",
                    ".|......|..",
                    ".L------J..",
                    "..........."
            ]

        when: "the edges are calculated"
            def loop = Sketch.parse(input).loop()
            def result = new Day10().getEdges(loop)

        then: "the result matches the example answer"
            result[0] == new Edge(new Position(1.0, 1.0), new Position(1.0, 7.0))
            result[1] == new Edge(new Position(1.0, 7.0), new Position(8.0, 7.0))
            result[2] == new Edge(new Position(8.0, 7.0), new Position(8.0, 4.0))
            result[3] == new Edge(new Position(8.0, 4.0), new Position(4.0, 4.0))
            result[4] == new Edge(new Position(4.0, 4.0), new Position(4.0, 1.0))
            result[5] == new Edge(new Position(4.0, 1.0), new Position(1.0, 1.0))
    }
}
