package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import kotlin.time.measureTime

data class Box(val number: Int, var lenses: LinkedHashSet<Lens> = linkedSetOf()) {

    fun remove(label: String) {
        lenses.removeIf { label == it.label }
    }

    fun add(lens: Lens) {
        val exists = lenses.any { it.label == lens.label }

        if(exists) {
            lenses.onEach {
                if (it.label == lens.label) {
                    it.focal = lens.focal
                }
            }
        } else {
            lenses.add(lens)
        }
    }
}

data class Lens(var label: String, var focal: Int)
data class Step(val label: String, val operation: Char, val focal: Int?) {
    companion object {
        @JvmStatic
        fun parse(step: String): Step {
            val (label, focal) = step.split("""[=\-]""".toRegex())
            return Step(label, if('-' in step) '-' else '=', if(focal == "") null else focal.toInt())
        }
    }
}
object HashAlgorithm {
    fun process(step: String): Int {
        var current = 0

        step.onEach { char ->
            current += char.code
            current *= 17
            current %= 256
        }

        return current
    }
}
object Day15 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(15, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Total load on north support beam (pt.1): $resultPartOne")
            println("Total load on north support beam (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Int {
        return input.first.split(",")
            .sumOf { HashAlgorithm.process(it) }
    }

    fun runPartTwo(input: List<String>): Int {
        val boxes = (0..255).associateWith { number -> Box(number) }

        input.first.split(",")
            .map { Step.parse(it) }
            .onEach { step ->
                val number = HashAlgorithm.process(step.label)
                val box = boxes[number] !!

                if(step.operation == '-') {
                    box.remove(step.label)
                }

                if(step.operation == '=') {
                    box.add(
                        Lens(step.label, step.focal !!)
                    )
                }
            }

        return boxes.entries
            .flatMap { (number, box) ->
                box.lenses.mapIndexed { index, lens ->
                    (number + 1) * (index + 1) * lens.focal
                }
            }
            .sum()
    }
}