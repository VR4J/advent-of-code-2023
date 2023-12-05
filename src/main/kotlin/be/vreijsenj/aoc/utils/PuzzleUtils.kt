package be.vreijsenj.aoc.utils

object PuzzleUtils {
    @JvmStatic
    fun getInput(day: Number, part: Number): List<String> {
        val file = javaClass.getResource(
            String.format("/inputs/days/%02d_%02d.txt", day, part)
        ) ?: return emptyList()

        return file.readText().lines()
    }

    @JvmStatic
    fun getInputAsText(day: Number, part: Number): String {
        val file = javaClass.getResource(
            String.format("/inputs/days/%02d_%02d.txt", day, part)
        ) ?: return ""

        return file.readText()
    }
}