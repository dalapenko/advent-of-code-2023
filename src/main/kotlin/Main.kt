import solution.Puzzle7

fun main() {
    val puzzle = Puzzle7(readFromRes(Puzzle7.INPUT_FILE_NAME))
    puzzle.printSecondPuzzleAnswer()
}


fun readFromRes(fileName: String): List<String> {
    val data = {}.javaClass.getResource(fileName)
        ?: throw IllegalArgumentException("Resource not found")

    return data.readText().lines()
}