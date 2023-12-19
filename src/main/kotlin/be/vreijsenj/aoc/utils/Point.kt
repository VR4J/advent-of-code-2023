package be.vreijsenj.aoc.utils

data class Point(val x: Int, val y: Int) {

    fun top() = Point(x, y - 1)
    fun right() = Point(x + 1, y)
    fun left() = Point(x - 1, y)
    fun bottom() = Point(x, y + 1)

    fun neighbours(): List<Point> {
        return listOf(top(), right(), left(), bottom())
    }

    fun next(direction: Direction): Point {
        return when(direction) {
            Direction.LEFT -> left()
            Direction.RIGHT -> right()
            Direction.DOWN -> bottom()
            Direction.UP -> top()
        }
    }
}
