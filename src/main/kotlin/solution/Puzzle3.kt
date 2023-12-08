package solution

import core.Puzzle
import core.decToZero
import core.incWithMax
import core.isInclude

class Puzzle3(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        var sumOfDetailsNumber = 0
        val markMap = mutableMapOf<Int, MutableList<IntRange>>() // карта меток деталей; линия - список позиций
        val numberMap = mutableMapOf<Int, MutableList<NumberData>>() // карта числел; линия - значение-вхождение

        val nonNumberOrDotRegex = "[^\\d.\\s]".toRegex()

        inputData.forEachIndexed { lineNumber, schemaInLine ->
            collectAllNumbersMap(numberMap, lineNumber, schemaInLine)
            collectSymbolRangeMap(nonNumberOrDotRegex, markMap, lineNumber, schemaInLine)
        }

        numberMap.forEach { (line, numbers) ->
            val nearLines = line.decToZero()..line.incWithMax(inputData.size - 1)
            nearLines.forEach CheckNearLines@{ nearLine ->
                val nearLineMarks = markMap[nearLine] ?: return@CheckNearLines

                numbers.forEach { detailNumber ->
                    val isNumberNearMark = nearLineMarks.any { it.isInclude(detailNumber.position) }

                    if (isNumberNearMark) sumOfDetailsNumber += detailNumber.value
                }
            }
        }

        return sumOfDetailsNumber
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        var sumOfGearsMultiply = 0
        val gearMap = mutableMapOf<Int, MutableList<IntRange>>() // карта меток деталей; линия - список позиций
        val numberMap = mutableMapOf<Int, MutableList<NumberData>>() // карта числел; линия - значение-вхождение

        val gearRegex = "[*]".toRegex()

        inputData.forEachIndexed { lineNumber, schemaInLine ->
            collectAllNumbersMap(numberMap, lineNumber, schemaInLine)
            collectSymbolRangeMap(gearRegex, gearMap, lineNumber, schemaInLine)
        }

        gearMap.forEach { (line, gearsInLine) ->
            val nearLines = line.decToZero()..line.incWithMax(inputData.size - 1)

            gearsInLine.forEach CheckGear@{ gear ->
                val numbersNearGear = mutableListOf<Int>()
                nearLines.forEach CheckNearLines@{ nearLine ->
                    val nearLineNumbers = numberMap[nearLine] ?: return@CheckNearLines

                    nearLineNumbers
                        .filter { it.position.isInclude(gear) }
                        .map { it.value }
                        .also { numbersNearGear.addAll(it) }
                }

                if (numbersNearGear.size < 2) return@CheckGear

                sumOfGearsMultiply += numbersNearGear.reduce { acc, i -> acc * i }
            }
        }

        return sumOfGearsMultiply
    }

    private fun collectAllNumbersMap(
        target: MutableMap<Int, MutableList<NumberData>>,
        lineNumber: Int,
        lineData: String
    ) {
        val numberRegex = "\\d+".toRegex()
        numberRegex.findAll(lineData).forEach {
            target
                .getOrPut(lineNumber, ::mutableListOf)
                .add(
                    NumberData(
                        value = it.value.toInt(),
                        position = IntRange(
                            start = it.range.first.decToZero(),
                            endInclusive = it.range.last.incWithMax(lineData.length - 1)
                        )
                    )
                )
        }
    }

    private fun collectSymbolRangeMap(
        searchRegex: Regex,
        target: MutableMap<Int, MutableList<IntRange>>,
        lineNumber: Int,
        lineData: String,
    ) {
        searchRegex.findAll(lineData).forEach {
            target
                .getOrPut(lineNumber, ::mutableListOf)
                .add(it.range)
        }
    }

    private class NumberData(
        val value: Int,
        val position: IntRange
    )

    companion object {
        const val INPUT_FILE_NAME = "puzzle_3_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_3_input.txt"
    }
}

