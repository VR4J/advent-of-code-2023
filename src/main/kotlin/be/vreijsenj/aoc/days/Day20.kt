package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.PuzzleUtils
import java.util.*
import kotlin.time.measureTime

data class Action(val sender: String, val pulses: Map<String, Pulse>)

enum class State {
    ON, OFF
}

enum class Pulse {
    HIGH, LOW
}

abstract class Module(open val name: String, open val destinations: List<String>, open var state: State = State.OFF) {

    companion object {
        @JvmStatic
        fun parse(line: String): Module {
            val (name, destinations) = line.split(" -> ")

            if(name == "broadcaster") {
                return Broadcaster(destinations.split(", "))
            }

            if(name[0] == '%') {
                return FlipFlop(name.filter { it != '%' }, destinations.split(", "))
            }

            if(name[0] == '&') {
                return Conjunction(name.filter { it != '&' }, destinations.split(", "))
            }

            throw IllegalStateException("Unknown module: '$line'")
        }
    }

    abstract fun receive(sender: String, pulse: Pulse): Map<String, Pulse>

    open fun init(modules: List<Module>): Module { return this }
}

data class FlipFlop(override val name: String, override val destinations: List<String>): Module(name, destinations) {

    override fun receive(sender: String, pulse: Pulse): Map<String, Pulse> {
        if(Pulse.HIGH == pulse) return emptyMap()

        return when(state) {
            State.OFF -> {
                state = State.ON
                destinations.associateWith { Pulse.HIGH }
            }
            State.ON -> {
                state = State.OFF
                destinations.associateWith { Pulse.LOW }
            }
        }
    }
}

data class Conjunction(override val name: String,  override val destinations: List<String>, var memory: MutableMap<String, Pulse> = mutableMapOf()): Module(name, destinations) {

    override fun receive(sender: String, pulse: Pulse): Map<String, Pulse> {
        memory[sender] = pulse

        if(memory.values.all { Pulse.HIGH == it }) {
            return destinations.associateWith { Pulse.LOW }
        }

        return destinations.associateWith { Pulse.HIGH }
    }

    override fun init(modules: List<Module>): Conjunction {
        memory = modules
            .filter { name in it.destinations }
            .map { it.name }
            .associateWith { Pulse.LOW }
            .toMutableMap()

        return this
    }
}

data class Broadcaster(override val destinations: List<String>): Module("broadcaster", destinations) {
    override fun receive(sender: String, pulse: Pulse): Map<String, Pulse> {
        return destinations.associateWith { pulse }
    }
}

object Day20 {

    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(20, 1)

            val resultPartOne = runPartOne(input)
            val resultPartTwo = runPartTwo(input)

            println("Sum of high and low pulses (pt.1): $resultPartOne")
            println("Sum of high and low pulses (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>): Long {
        val modules = input.map { Module.parse(it) }
        val registry = modules.map { it.init(modules) }.associateBy { it.name }

        val broadcaster = registry.values.first { it is Broadcaster }

        val pulses = mapOf(Pulse.LOW to 0L, Pulse.HIGH to 0L).toMutableMap()

        (1..1000).onEach {
            val queue: Queue<Action> = LinkedList(
                listOf(Action("button", mapOf(broadcaster.name to Pulse.LOW)))
            )

            while(queue.isNotEmpty()) {
                send(queue, registry, pulses)
            }
        }

        return pulses.values.reduce(Long::times)
    }

    fun runPartTwo(input: List<String>): Long {
        return 0L
    }

    fun send(queue: Queue<Action>, modules: Map<String, Module>, pulses: MutableMap<Pulse, Long>) {
        val action = queue.remove()

        action.pulses.values.onEach { pulse ->
            pulses[pulse] = pulses.getOrDefault(pulse, 0) + 1
        }

        action.pulses.entries
            .filter { (destination, pulse) -> destination in modules }
            .map { (destination, pulse) ->
                val next = modules.getValue(destination)
                    .receive(action.sender, pulse)

                Action(destination, next)
            }
            .onEach { next -> queue.add(next) }
    }
}