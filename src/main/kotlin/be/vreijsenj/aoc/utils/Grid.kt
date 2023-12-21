package be.vreijsenj.aoc.utils

open class Grid(
    open val points: List<Point>,
    open val xMin: Int = points.minOf { it.x }, val xMax: Int = points.maxOf { it.x },
    open val yMin: Int = points.minOf { it.y }, val yMax: Int = points.maxOf { it.y }
) {

    companion object {

        @JvmStatic
        fun parse(xMax: Int, yMax: Int, xMin: Int = 0, yMin: Int = 0): Grid {
            return Grid(emptyList(), xMin, xMax, yMin, yMax)
        }

        fun rasterize(input: List<String>): List<Point> {
            return rasterize(input) { x, y, _ -> Point(x, y) }
        }

        fun <T> rasterize(input: List<String>, transformer: (Int, Int, Char) -> T): List<T> {
            val rows = input.map { it.toCharArray() }

            return rows.first.indices
                .flatMap { xIndex ->
                    rows.mapIndexed { yIndex, row ->
                        transformer(xIndex, yIndex, row[xIndex])
                    }
                }
        }
    }

    operator fun Grid.contains(point: Point): Boolean {
        return point.x in xMin..xMax && point.y in yMin..yMax
    }

    fun points(): List<Point> =
        points.ifEmpty {
            (xMin..xMax).flatMap { x ->
                (yMax..yMax).map { y ->
                    Point(x, y)
                }
            }
        }
}
