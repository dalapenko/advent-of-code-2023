package solution

import core.Puzzle
import core.replaceAll

class Puzzle1(inputData: List<String>) : Puzzle(inputData) {

    private val digitReplaceMap = mapOf(
        "one" to "one1one", "two" to "two2two", "three" to "three3three",
        "four" to "four4four", "five" to "five5five", "six" to "six6six",
        "seven" to "seven7seven", "eight" to "eight8eight", "nine" to "nine9nine"
    )

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        return inputData.sumOf(::getDigitInLine)
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        return inputData
            .map(::replaceLetterDigits)
            .sumOf(::getDigitInLine)
    }

    private fun getDigitInLine(inputLine: String): Int {
        val allDigits = ONLY_DIGIT_REGEX_PATTER.toRegex()
            .findAll(inputLine.lowercase())

        val (first, last) = allDigits.first().value to allDigits.last().value

        return "$first$last".toInt()
    }

    private fun replaceLetterDigits(inputLine: String): String {
        return inputLine.replaceAll(digitReplaceMap)
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_1_input.txt"
    }
}

private const val ONLY_DIGIT_REGEX_PATTER = "[1-9]"
