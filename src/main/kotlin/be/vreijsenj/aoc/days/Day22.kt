package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.Cube
import be.vreijsenj.aoc.utils.PuzzleUtils
import java.util.*
import kotlin.collections.Set
import kotlin.time.measureTime

data class Brick(var cubes: List<Cube>) {
    companion object {
        @JvmStatic
        fun parse(line: String): Brick {
            val (start, end) = line.split("~")
            val (xStart, yStart, zStart) = start.split(",").map { it.toLong() }
            val (xEnd, yEnd, zEnd) = end.split(",").map { it.toLong() }

            val cubes = (xStart..xEnd).flatMap { x ->
                (yStart..yEnd).flatMap { y ->
                    (zStart..zEnd).map { z ->
                        Cube(x.toDouble(), y.toDouble(), z.toDouble())
                    }
                }
            }

            return Brick(cubes = cubes)
        }
    }

    fun supports(brick: Brick): Boolean {
        if(brick == this) return false

        return cubes.any { it.above() in brick.cubes }
    }

    fun supportedBy(bricks: List<Brick>): List<Brick> {
        return bricks.filter { it != this }
            .filter { brick -> brick.cubes.map { it.above() }.any { it in cubes } }
    }

    fun fall(others: List<Cube>) {
        while(cubes.map { it.below() }.all { it !in others && it.z > 0  }) {
            cubes = cubes.map { it.below() }
        }
    }
}

object Day22 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(22, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Amount of blocks that can be disintegrated safely (pt.1): $resultPartOne")
            println("Amount of blocks that will fall when disintegrated (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        var bricks = input.map { Brick.parse(it) }.sortedBy { it.cubes.minOf { it.z } }

        bricks = fall(bricks)

        val map = getBrickSupportMap(bricks)
        val safe = map.filter { (brick, supports) ->
            supports.isEmpty() || hasAlternativeSupport(brick, map, supports)
        }

        return safe.size
    }

    fun runPartTwo(input: List<String>): Int {
        var bricks = input.map { Brick.parse(it) }.sortedBy { it.cubes.minOf { it.z } }

        bricks = fall(bricks)

        val supports = getBrickSupportMap(bricks)
        val supportBy = getBrickSupportByMap(bricks)

        return bricks.sumOf { brick ->
            val fell = mutableSetOf(brick)
            val falling = supports.getValue(brick)
                .filter { ! hasSupportStillStanding(it, supportBy, fell) }

            val queue: Queue<Brick> = LinkedList(falling)

            while(queue.isNotEmpty()) {
                val tumbling = queue.remove()

                fell.add(tumbling)

                supports.getValue(tumbling)
                    .filter { ! hasSupportStillStanding(it, supportBy, fell) }
                    .onEach { queue.add(it) }
            }

            // Exclude original brick that was disintegrated
            fell.size - 1
        }
    }

    private fun hasSupportStillStanding(brick: Brick, supportByMap: Map<Brick, List<Brick>>, fell: Set<Brick>): Boolean {
        val supportedBy = supportByMap[brick] ?: emptyList()

        return supportedBy.any { it !in fell }
    }

    private fun hasAlternativeSupport(brick: Brick, map: Map<Brick, List<Brick>>, supports: List<Brick>): Boolean {
        return supports.all { isLookingForSupport ->
            map.entries.any { it.key != brick && isLookingForSupport in it.value }
        }
    }

    private fun fall(bricks: List<Brick>): List<Brick> {
        return bricks.onEach { brick ->
            val cubes = bricks.filter { it != brick }.flatMap { it.cubes }
            brick.fall(cubes)
        }
    }

    private fun getBrickSupportMap(bricks: List<Brick>): Map<Brick, List<Brick>> {
        return bricks.associateWith { brick ->
            bricks.filter { brick.supports(it) }
        }
    }

    private fun getBrickSupportByMap(bricks: List<Brick>): Map<Brick, List<Brick>> {
        return bricks.associateWith { it.supportedBy(bricks) }
    }
}