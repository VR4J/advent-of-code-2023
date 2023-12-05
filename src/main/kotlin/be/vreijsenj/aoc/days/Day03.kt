package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

data class EngineSchematicLine(val numbers: List<PartNumber>, val symbols: List<Symbol>) {
    companion object {
        val EMPTY = EngineSchematicLine(emptyList(), emptyList())

        @JvmStatic
        fun parse(line: String): EngineSchematicLine {
            val numbers = PartNumber.PATTERN.findAll(line)
            val symbols = Symbol.PATTERN.findAll(line)

            return EngineSchematicLine(
                numbers = numbers.map { PartNumber(it.value.toInt(), it.range) }.toList(),
                symbols = symbols.map { Symbol(it.value, it.range.first) }.toList()
            )
        }
    }
}
data class Symbol(val value: String, val location: Int) {
    companion object {
        val PATTERN = """[^.\d]""".toRegex()
    }

    fun isAdjacentTo(target: IntRange): Boolean {
        return location in IntRange(target.first - 1, target.last + 1)
    }
}
data class PartNumber(val value: Int, val location: IntRange) {
    companion object {
        val PATTERN = """\d+""".toRegex()
    }

    fun isAdjacentTo(target: Int): Boolean {
        return target in IntRange(location.first - 1, location.last + 1)
    }

    fun isValid(symbols: List<Symbol>): Boolean {
        return symbols.any {
            symbol -> symbol.isAdjacentTo(location)
        }
    }
}
object Day03 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(3, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of all engine parts (pt.1): $resultPartOne")
            println("Sum of all gear ratios (pt.2): $resultPartTwo")
        }

        println("Took $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        val schematic = input.asSequence()
            .map(EngineSchematicLine::parse)
            .toList()

        return schematic.indices.asSequence()
            .flatMap {  index ->
                val (previous, current, next) = getSurrounding(schematic, index)

                current.numbers.filter {
                    it.isValid(previous.symbols) || it.isValid(current.symbols) || it.isValid(next.symbols)
                }
            }
            .map(PartNumber::value)
            .sum()
    }

    fun runPartTwo(input: List<String>): Int {
        val schematic = input.asSequence()
            .map(EngineSchematicLine::parse)
            .toList()

        return schematic.indices.asSequence()
            .flatMap {  index ->
                val (previous, current, next) = getSurrounding(schematic, index)

                current.symbols
                    .filter { it.value == "*" }
                    .map { getGears(it, previous.numbers, current.numbers, next.numbers) }
                    .filter { it.isNotEmpty() }
                    .map { it.first.value * it.last.value }
            }
            .sum()
    }

    private fun getSurrounding(schematic: List<EngineSchematicLine>, index: Int): Triple<EngineSchematicLine, EngineSchematicLine, EngineSchematicLine> {
        val previous = schematic.getOrElse(index - 1) { EngineSchematicLine.EMPTY }
        val current = schematic[index]
        val next = schematic.getOrElse(index + 1) { EngineSchematicLine.EMPTY }

        return Triple(previous, current, next)
    }

    private fun getGears(symbol: Symbol, previous: List<PartNumber>, current: List<PartNumber>, next: List<PartNumber>): List<PartNumber> {
        val matches = mutableListOf<PartNumber>()

        previous.filter { it.isAdjacentTo(symbol.location) }
            .onEach(matches::add)

        current.filter { it.isAdjacentTo(symbol.location) }
            .onEach(matches::add)

        next.filter { it.isAdjacentTo(symbol.location) }
            .onEach(matches::add)

        if(matches.size == 2) {
            return matches
        }

        return emptyList()
    }
}