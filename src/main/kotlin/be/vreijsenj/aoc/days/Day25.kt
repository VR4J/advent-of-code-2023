package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph
import kotlin.time.measureTime

data class Connection(val left: String, val right: String) {
    companion object {
        @JvmStatic
        fun parse(line: String): List<Connection> {
            val (left, connectors) = line.split(": ")
            return connectors.split(" ").map { Connection(left, it) }
        }
    }
}

data class WireDiagram(val connections: List<Connection>) {

    companion object {
        @JvmStatic
        fun parse(input: List<String>): WireDiagram {
            val connections = input.flatMap { Connection.parse(it) }
            return WireDiagram(connections = connections)
        }
    }
}

object Day25 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(25, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Amount of intersections within test area (pt.1): $resultPartOne")
            println("Amount of intersections within test area (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val diagram = WireDiagram.parse(input)

        val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        diagram.connections.onEach { connection ->
            if(connection.left !in graph.vertexSet()) {
                graph.addVertex(connection.left)
            }

            graph.addVertex(connection.right)
            graph.addEdge(connection.left, connection.right)
        }

        val vertices = StoerWagnerMinimumCut(graph).minCut()

        return (graph.vertexSet().size - vertices.size) * vertices.size
    }

    fun runPartTwo(input: List<String>): Long {
        return 0L
    }
}