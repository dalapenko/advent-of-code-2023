import solution.Puzzle8

fun main() {
    val puzzle = Puzzle8(readFromRes(Puzzle8.INPUT_FILE_NAME))
    puzzle.printFirstPuzzleAnswer()
}

fun readFromRes(fileName: String): List<String> {
    val data = {}.javaClass.getResource(fileName)
        ?: throw IllegalArgumentException("Resource not found")

    return data.readText().lines()
}