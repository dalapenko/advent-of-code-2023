package core

abstract class Puzzle(
    private val inputData: List<String>
) {
    protected abstract fun firstPuzzleSolution(inputData: List<String>): Any
    protected abstract fun secondPuzzleSolution(inputData: List<String>): Any

    fun printFirstPuzzleAnswer() {
        println(firstPuzzleSolution(inputData).toString())
    }

    fun printSecondPuzzleAnswer() {
        println(secondPuzzleSolution(inputData).toString())
    }
}