package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

data class CheckDefinition(val property: Char, val isGreaterThen: Boolean, val value: Long, val destination: String)

data class Workflow(val name: String, val checks: List<(Part) -> String?>, val definitions: List<CheckDefinition>, val alternative: String) {

    companion object {

        @JvmStatic
        fun parse(line: String): Workflow {
            val (name, instructions) = line.split("""[{}]""".toRegex())

            val checks = instructions.split(",")
                .map { instruction ->
                    if(":" in instruction) {
                        val (predicate, workflow) = instruction.split(":")
                        val (property, value) = predicate.split("""[><]""".toRegex())

                        if(">" in predicate) {
                            { part: Part ->
                                when(property) {
                                    "x" -> if(part.x > value.toLong()) workflow else null
                                    "m" -> if(part.m > value.toLong()) workflow else null
                                    "a" -> if(part.a > value.toLong()) workflow else null
                                    "s" -> if(part.s > value.toLong()) workflow else null
                                    else -> null
                                }
                            }
                        } else {
                            { part: Part ->
                                when(property) {
                                    "x" -> if(part.x < value.toLong()) workflow else null
                                    "m" -> if(part.m < value.toLong()) workflow else null
                                    "a" -> if(part.a < value.toLong()) workflow else null
                                    "s" -> if(part.s < value.toLong()) workflow else null
                                    else -> null
                                }
                            }
                        }
                    } else {
                        { _: Part -> instruction }
                    }
                }

            val definitions = instructions.split(",")
                .filter { ":" in it }
                .map { instruction ->
                    val (predicate, workflow) = instruction.split(":")
                    val (property, value) = predicate.split("""[><]""".toRegex())

                    CheckDefinition(
                        property = property.first(),
                        isGreaterThen = ">" in predicate,
                        value = value.toLong(),
                        destination = workflow
                    )
                }

            val alternative = instructions.split(",")
                .first { ":" !in it }

            return Workflow(
                name = name,
                checks = checks,
                definitions = definitions,
                alternative = alternative
            )
        }
    }
}

data class Part(val x: Long, val m: Long, val a: Long, val s: Long) {
    companion object {

        @JvmStatic
        fun parse(line: String): Part {
            val properties = line.replace("""[{}]""".toRegex(), "").split(",")

            return Part(
                x = properties.first { it.startsWith("x") }.split("=").last.toLong(),
                m = properties.first { it.startsWith("m") }.split("=").last.toLong(),
                a = properties.first { it.startsWith("a") }.split("=").last.toLong(),
                s = properties.first { it.startsWith("s") }.split("=").last.toLong(),
            )
        }
    }

    fun value(): Long {
        return x + m + a + s
    }
}

object Day19 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val inputPartOne = PuzzleUtils.getInputAsText(19, 1)
            val inputPartTwo = PuzzleUtils.getInput(19, 2)

            val resultPartOne = runPartOne(inputPartOne)
            val resultPartTwo = runPartTwo(inputPartTwo)

            println("Sum of all part properties (pt.1): $resultPartOne")
            println("Sum of all possible combinations (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: String): Long {
        val (workflowsInstructionText, partsRatingText) = input.split("""\n\n""".toRegex())

        val parts = partsRatingText.lines().map { Part.parse(it) }
        val workflows = workflowsInstructionText.lines()
            .map { Workflow.parse(it) }
            .associateBy { it.name }

        return parts
            .mapNotNull { run(it, "in", workflows) }
            .sumOf { it.value() }
    }

    fun runPartTwo(input: List<String>): Long {
        val workflows = input
            .map { Workflow.parse(it) }
            .associateBy { it.name }

        val possibilities = mutableMapOf(
            Pair('x', 1..4000L),
            Pair('m', 1..4000L),
            Pair('a', 1..4000L),
            Pair('s', 1..4000L),
        )

        return combinations("in", workflows, possibilities)
    }

    private fun combinations(destination: String, workflows: Map<String, Workflow>, possibilities: MutableMap<Char, LongRange>): Long {
        if(destination == "A") {
            return possibilities.values.map { it.size() }.reduce(Long::times)
        }

        if(destination == "R") {
            return 0
        }

        val workflow = workflows[destination] !!

        val copy = possibilities.toMutableMap()

        val result = workflow.definitions.sumOf { definition ->
            val matching = if(definition.isGreaterThen) {
                copy.getValue(definition.property).merge((definition.value + 1)..4000)
            } else {
                copy.getValue(definition.property).merge(1..< definition.value)
            }

            val mismatching = if(definition.isGreaterThen) {
                copy.getValue(definition.property).merge(1.. definition.value)
            } else {
                copy.getValue(definition.property).merge(definition.value..4000)
            }

            copy[definition.property] = matching

            combinations(definition.destination, workflows, copy)
                // Prepare matching for next definition, and do not include the ones that before
                .also { copy[definition.property] = mismatching }
        }

        return result + combinations(workflow.alternative, workflows, copy)
    }

    private tailrec fun run(part: Part, destination: String, workflows: Map<String, Workflow>): Part? {
        if(destination == "A") {
            return part
        }

        if(destination == "R") {
            return null
        }

        val workflow = workflows[destination] !!

        val next = workflow.checks
            .mapNotNull { it(part) }
            .first !!

        return run(part, next, workflows)
    }

    private fun LongRange.size() = last - start + 1

    private fun LongRange.merge(other: LongRange) = (maxOf(first, other.first)..minOf(last, other.last))
}