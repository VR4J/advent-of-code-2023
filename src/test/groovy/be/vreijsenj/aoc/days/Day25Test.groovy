package be.vreijsenj.aoc.days

import spock.lang.Specification

class Day25Test extends Specification {

    def "returns the factor of the two separate wire groups"() {
        given: "the wiring diagram"
            def input = [
                    "jqt: rhn xhk nvd",
                    "rsh: frs pzl lsr",
                    "xhk: hfx",
                    "cmg: qnr nvd lhk bvb",
                    "rhn: xhk bvb hfx",
                    "bvb: xhk hfx",
                    "pzl: lsr hfx nvd",
                    "qnr: nvd",
                    "ntq: jqt hfx bvb xhk",
                    "nvd: lhk",
                    "lsr: lhk",
                    "rzs: qnr cmg lsr rsh",
                    "frs: qnr lhk lsr"
            ]

        when: "the diagram is analysed"
            def result = new Day25().runPartOne(input)

        then: "the result matches the example answer"
            result == 54
    }
}
