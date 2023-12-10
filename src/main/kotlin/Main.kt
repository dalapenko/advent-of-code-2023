import solution.Puzzle9

fun main() {
    val puzzle = Puzzle9(readFromRes(Puzzle9.INPUT_FILE_NAME))
    puzzle.printSecondPuzzleAnswer()
}

fun readFromRes(fileName: String): List<String> {
    val data = {}.javaClass.getResource(fileName)
        ?: throw IllegalArgumentException("Resource not found")

    return data.readText().lines()
}