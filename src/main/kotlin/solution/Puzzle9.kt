package solution

import core.Puzzle
import core.parseIntList

class Puzzle9(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        return inputData.map { line ->
            val numbers = line.parseIntList()

            createValuesToZeroMap(numbers)
        }.sumOf { lines ->
            lines.sumOf { it.last() }
        }
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        return inputData.map { line ->
            val numbers = line.parseIntList()

            createValuesToZeroMap(numbers)
        }.sumOf { line ->
            line.reversed()
                .map { it.first() }
                .reduce { acc, i -> i - acc }
        }
    }

    private fun createValuesToZeroMap(
        inputValues: List<Int>
    ): List<List<Int>> {
        var currentValues = inputValues
        val outputValues = mutableListOf(currentValues)

        while (currentValues.any { it != 0 }) {
            currentValues = currentValues.windowed(2)
                .map { it.last() - it.first() }

            if (currentValues.isEmpty()) continue
            outputValues.add(currentValues)
        }

        return outputValues
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_9_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_9_input.txt"
    }
}