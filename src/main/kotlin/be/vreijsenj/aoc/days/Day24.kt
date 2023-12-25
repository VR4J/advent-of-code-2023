package be.vreijsenj.aoc.days

import be.vreijsenj.aoc.utils.Cube
import be.vreijsenj.aoc.utils.PuzzleUtils
import com.microsoft.z3.Context
import com.microsoft.z3.RatNum
import kotlin.time.measureTime

data class Hailstone(val position: Cube, val velocity: Triple<Double, Double, Double>) {

    companion object {
        @JvmStatic
        fun parse(line: String): Hailstone {
            val (position, velocity) = line.split(" @ ")
            val (px, py, pz) = position.split(", ").map { it.trim() }.map { it.toDouble() }
            val (vx, vy, vz) = velocity.split(", ").map { it.trim() }.map { it.toDouble() }

            return Hailstone(
                position = Cube(px, py, pz),
                velocity = Triple(vx, vy, vz)
            )
        }
    }
}

object Day24 {
    @JvmStatic
    fun main(args: Array<String>) {
        val elapsed = measureTime {
            val input = PuzzleUtils.getInput(24, 1)

            val resultPartOne = runPartOne(input, 200000000000000.0, 400000000000000.0)
            val resultPartTwo = runPartTwo(input)

            println("Amount of intersections within test area (pt.1): $resultPartOne")
            println("Amount of intersections within test area (pt.2): $resultPartTwo")
        }

        println("Took: $elapsed")
    }

    fun runPartOne(input: List<String>, min: Double, max: Double): Long {
        val hail = input.map { Hailstone.parse(it) }

        val intersections = combinations(hail)
            .mapNotNull { (left, right) ->
                val a1 = left.velocity.second / left.velocity.first
                val b1 = left.position.y - a1 * left.position.x

                val a2 = right.velocity.second / right.velocity.first
                val b2 = right.position.y - a2 * right.position.x

                val px = (b2 - b1) / (a1 - a2)
                val py = px * a1 + b1

                val inFuture = (px > left.position.x) == (left.velocity.first > 0) && (px > right.position.x) == (right.velocity.first > 0)

                if(inFuture) Pair(px, py) else null
            }
            .filter { it.first > min && it.first < max && it.second > min && it.second < max }
            .toList()

        return intersections.size.toLong()
    }

    fun runPartTwo(input: List<String>): Long {
        // <rock position>  =  <hailstone position>
        //   x + t * vx    =      hx + t * hvx
        //   y + t * vy    =      hy + t * hvy
        //   z + t * vz    =      hz + t * hvz

        val hail = input.map { Hailstone.parse(it) }

        val context = Context()
        val solver = context.mkSolver()

        val (x, y, z) = Triple(context.mkRealConst("x"), context.mkRealConst("y"), context.mkRealConst("z"))
        val (vx, vy, vz) = Triple(context.mkRealConst("vx"), context.mkRealConst("vy"), context.mkRealConst("vz"))

        hail.onEachIndexed { i, hailstone ->
                val t = context.mkRealConst("t$i")
                val hx = context.mkReal(hailstone.position.x.toLong())
                val hvx = context.mkReal(hailstone.velocity.first.toLong())

                val hy = context.mkReal(hailstone.position.y.toLong())
                val hvy = context.mkReal(hailstone.velocity.second.toLong())

                val hz = context.mkReal(hailstone.position.z.toLong())
                val hvz = context.mkReal(hailstone.velocity.third.toLong())

                solver.add(
                    context.mkGe(t, context.mkInt(0L))
                )
                solver.add(
                    context.mkEq(
                        context.mkAdd(x, context.mkMul(vx, t)),
                        context.mkAdd(hx, context.mkMul(hvx, t))
                    )
                )
                solver.add(
                    context.mkEq(
                        context.mkAdd(y, context.mkMul(vy, t)),
                        context.mkAdd(hy, context.mkMul(hvy, t))
                    )
                )
                solver.add(
                    context.mkEq(
                        context.mkAdd(z, context.mkMul(vz, t)),
                        context.mkAdd(hz, context.mkMul(hvz, t))
                    )
                )
            }

        println(solver.check())

        val model = solver.model

        val rx = model.eval(x, true) as RatNum
        val ry = model.eval(y, true) as RatNum
        val rz = model.eval(z, true) as RatNum

        return rx.numerator.int64 + ry.numerator.int64 + rz.numerator.int64
    }

    private fun <T> combinations(arr: List<T>): Sequence<Pair<T, T>> = sequence {
        for(i in 0 until arr.size-1)
            for(j in i+1 until arr.size)
                yield(arr[i] to arr[j])
    }
}