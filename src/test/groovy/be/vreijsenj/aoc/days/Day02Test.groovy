package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day02Test extends Specification {

    def "parses the game input correctly"() {
        given: "the game input"
            def input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"

        when: "parsing"
            def game = Game.parse(input)

        then: "the result is stored in a Game object"
            game instanceof Game
            game.id == "1"
            game.sets.size() == 3
            game.sets.first.blue == 3
            game.sets.first.red == 4
            game.sets.first.green == 0
    }

    def "returns the possible games given the bag contents"() {
        given: "the game sets, and bag content"
            def bagContent = new Bag(12, 13, 14)
            def games = [
                    "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                    "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                    "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                    "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                    "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
            ]

        when: "the games are played"
            def result = new Day02().runPartOne(games, bagContent)

        then: "the sum of the possible game ids are returned"
            result == 8
    }

    def "returns the sum of the power of the minimum set of cubes"() {
        given: "the game sets"
            def games = [
                    "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                    "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                    "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                    "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                    "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
            ]

        when: "the games are played"
            def result = new Day02().runPartTwo(games)

        then: "the sum of the power of the minimum set of cubes is returned"
            result == 2286
    }
}
