package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlinx.coroutines.*
import java.time.Instant
import kotlin.time.measureTime

data class Almanac(
    val soil: List<Mapping>,
    val fertilizer: List<Mapping>,
    val water: List<Mapping>,
    val light: List<Mapping>,
    val temperature: List<Mapping>,
    val humidity: List<Mapping>,
    val location: List<Mapping>
) {
    companion object {
        fun parse(input: String): Almanac {
            return Almanac(
                soil = getMapping(input, "seed-to-soil"),
                fertilizer = getMapping(input, "soil-to-fertilizer"),
                water = getMapping(input, "fertilizer-to-water"),
                light = getMapping(input, "water-to-light"),
                temperature = getMapping(input, "light-to-temperature"),
                humidity = getMapping(input, "temperature-to-humidity"),
                location = getMapping(input, "humidity-to-location"),
            )
        }

        private fun getMapping(input: String, name: String): List<Mapping> {
            return input.split("""\n(\s+)?\n""".toRegex())
                .filter { it.contains(name) }
                .flatMap { it.lines() }
                .drop(1)
                .filter { it.isNotBlank() }
                .map { Mapping.parse(it) }
        }
    }
}
data class Seed(val id: Long, val location: Long) {
    companion object {
        val RANGE_PATTERN = """\d+\s\d+""".toRegex()

        @JvmStatic
        fun parse(id: Long, almanac: Almanac): Seed {
            val soil = getMappingValue(id, almanac.soil)
            val fertilizer = getMappingValue(soil, almanac.fertilizer)
            val water = getMappingValue(fertilizer, almanac.water)
            val light = getMappingValue(water, almanac.light)
            val temperature = getMappingValue(light, almanac.temperature)
            val humidity = getMappingValue(temperature, almanac.humidity)
            val location = getMappingValue(humidity, almanac.location)

            return Seed(id, location)
        }

        private fun getMappingValue(source: Long, mapping: List<Mapping>): Long {
            return mapping.filter { source in it.source }
                .map { it.getValue(source) }
                .firstOrNull() ?: source
        }
    }
}

data class Mapping(val destination: LongRange, val source: LongRange) {
    companion object {

        @JvmStatic
        fun parse(line: String): Mapping {
            val (destinationStart, sourceStart, length) = line.trim().split(" ").map { it.toLong() }

            return Mapping(
                destination = LongRange(destinationStart, destinationStart + length - 1),
                source = LongRange(sourceStart, sourceStart + length - 1)
            )
        }
    }

    fun getValue(source: Long): Long {
        val position = source - this.source.first
        return this.destination.first + position
    }
}
object Day05 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInputAsText(5, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Closest seed location (pt.1): $resultPartOne")
            println("Closest seed location (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: String): Long {
        val almanac = Almanac.parse(input)

        val seeds = input.lines().first.split(" ")
            .filter { it.isNumeric() }
            .map { Seed.parse(it.toLong(), almanac) }

        return seeds.minOf { it.location }
    }

    fun runPartTwo(input: String): Long {
        val almanac = Almanac.parse(input)

        val ranges = Seed.RANGE_PATTERN.findAll(input.lines().first)
            .map { it.value.split(" ") }
            .map { it.first.toLong()..it.first.toLong() + it.last.toLong() }
            .toList()

        return runBlocking(Dispatchers.Default) {
            ranges
                .pmap { range ->
                    range.minOf { Seed.parse(it, almanac).location }
                }
                .min()
        }
    }

    private fun String.isNumeric(): Boolean {
        return toDoubleOrNull() != null
    }

    suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
        map { async { f(it) } }.awaitAll()
    }
}