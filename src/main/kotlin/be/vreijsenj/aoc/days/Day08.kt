package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.math.floor
import kotlin.time.measureTime

enum class Direction(val value: Int) {
    LEFT(0),
    RIGHT(1);

    companion object {
        @JvmStatic
        fun parse(char: String): Direction {
            return when (char) {
                "L" -> LEFT
                "R" -> RIGHT
                else -> throw IllegalStateException()
            }
        }
    }
}
data class Node(val name: String, val left: String, val right: String) {
    companion object {
        @JvmStatic
        fun parse(input: String): Node {
            val (name, nodes) = input.split(" = ")
            val (left, right) = nodes.split(", ")
                .map { it.replace("""[()]""".toRegex(), "") }

            return Node(name, left, right)
        }
    }
}
data class Network(val directions: List<Direction>, val nodes: Map<String, Node>) {
    companion object {
        @JvmStatic
        fun parse(input: List<String>): Network {
            return Network(
                directions = input.first.chunked(1).map { Direction.parse(it) },
                nodes = input.drop(2)
                    .map { Node.parse(it) }
                    .associateBy { it.name }
            )
        }
    }

    fun getStepDistance(start: Node, hasArrived: (Node) -> Boolean): Long {
        var current = start
        var steps = 0L

        while(true) {
            steps++

            val direction = getDirection(steps)

            current = when(direction) {
                Direction.LEFT -> nodes.get(current.left)!!
                Direction.RIGHT -> nodes.get(current.right)!!
            }

            if(hasArrived(current)) {
                return steps
            }
        }
    }

    fun getDirection(step: Long): Direction {
        if(step > directions.size) {
            val times = floor(step.toDouble() / directions.size)
            val inRangeStep = step - (directions.size * times)

            if(inRangeStep == 0.0) {
                return directions.last
            }

            return getDirection(inRangeStep.toLong())
        }

        return directions[(step - 1).toInt()]
    }
}
object Day08 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(8, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Total steps to reach destination (pt.1): $resultPartOne")
            println("Total steps to reach destination (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Long {
        val network = Network.parse(input)

        return network.getStepDistance(network.nodes["AAA"]!!) { it.name.endsWith("Z") }
    }

    fun runPartTwo(input: List<String>): Long {
        val network = Network.parse(input)

        return network.nodes
            .filter { it.key.endsWith("A") }
            .map { network.getStepDistance(it.value) { node -> node.name.endsWith("Z") } }
            .reduce(this::lcm)
    }

    /**
     * Retrieves the Greatest Common Divisor (https://en.wikipedia.org/wiki/Greatest_common_divisor)
     */
    private fun gcd(left: Long, right: Long): Long {
        var gcd = 1L
        var divisor = 1L

        while (divisor <= left && divisor <= right ) {
            // Checks if divisor is factor of both integers
            if (left % divisor == 0L && right % divisor == 0L) {
                gcd = divisor
            }

            ++divisor
        }

        return gcd
    }

    /**
     * Retrieves the Least Common Multiplier (https://en.wikipedia.org/wiki/Least_common_multiple)
     */
    private fun lcm(left: Long, right: Long): Long {
        return left * right / gcd(left, right)
    }
}