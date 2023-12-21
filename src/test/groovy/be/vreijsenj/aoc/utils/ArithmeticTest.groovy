package be.vreijsenj.aoc.utils

import spock.lang.Specification

class ArithmeticTest extends Specification {

    def "calculates the least common multiple (lcm) correctly"() {
        given: "two input numbers"
        def left = 4
        def right = 6

        when: "the lcm is calculated"
        def result = Arithmetic.lcm(left, right)

        then: "the result is matches the example"
        result == 12
    }

    def "calculates the greatest common divisor (gcd) correctly"() {
        given: "two input numbers"
        def left = 8
        def right = 12

        when: "the lcm is calculated"
        def result = Arithmetic.gcd(left, right)

        then: "the result is matches the example"
        result == 4
    }
}
