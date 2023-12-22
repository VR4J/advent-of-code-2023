package be.vreijsenj.aoc.utils

open class Cube(open val x: Long, open val y: Long, open val z: Long) {

    fun above() = Cube(x, y, z + 1)
    fun below() = Cube(x, y, z - 1)

    override fun equals(other: Any?): Boolean {
        if(other !is Cube) return false

        return x == other.x && y == other.y && z == other.z
    }
}