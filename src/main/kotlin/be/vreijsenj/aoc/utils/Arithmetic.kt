package be.vreijsenj.aoc.utils

object Arithmetic {

    /**
     * Retrieves the Greatest Common Divisor (https://en.wikipedia.org/wiki/Greatest_common_divisor)
     */
    @JvmStatic
    fun gcd(left: Long, right: Long): Long {
        var gcd = 1L
        var divisor = 1L

        while (divisor <= left && divisor <= right ) {
            // Checks if divisor is factor of both numbers
            if (left % divisor == 0L && right % divisor == 0L) {
                gcd = divisor
            }

            ++divisor
        }

        return gcd
    }

    /**
     * Retrieves the Least Common Multiple (https://en.wikipedia.org/wiki/Least_common_multiple)
     * using the Greatest Common Divisor (https://en.wikipedia.org/wiki/Greatest_common_divisor)
     */
    @JvmStatic
    fun lcm(left: Long, right: Long): Long {
        return left * right / gcd(left, right)
    }
}