package solution

import core.Puzzle
import core.findAllValue
import core.lcm

typealias StepMap = Map<String, Pair<String, String>>

class Puzzle8(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        val (flow, map) = readInputData(inputData)
        val stepMap = createStepMap(map)

        return stepMap.countByFlow(stepFlow = flow, from = "AAA") { stepValue ->
            stepValue == "ZZZ"
        }
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val (flow, map) = readInputData(inputData)
        val stepMap = createStepMap(map)
        val startedPoints = stepMap.keys.filter { it.endsWith("A") }

        return startedPoints.map { start ->
            stepMap.countByFlow(flow, start) {
                it.endsWith("Z")
            }
        }.lcm()
    }

    private fun readInputData(inputData: List<String>): Pair<List<String>, List<String>> {
        return "\\w".toRegex().findAllValue(inputData.first()).toList() to inputData.drop(2)
    }

    private fun createStepMap(map: List<String>): StepMap {
        return map.associate { line ->
            with("\\w+".toRegex().findAll(line).map(MatchResult::value)) {
                first() to with(drop(1)) { first() to last() }
            }
        }
    }

    private fun StepMap.countByFlow(
        stepFlow: List<String>,
        from: String,
        to: (String) -> Boolean
    ): Long {
        var stepCounter = 0L

        var currentStep: String? = from

        while (currentStep != null && !to(currentStep)) {
            val stepIndex = (stepCounter % stepFlow.size).toInt()
            val nextStep = stepFlow[stepIndex]

            val (left, right) = this[currentStep] ?: (null to null)

            currentStep = if (nextStep == LEFT_STEP) left else right

            stepCounter++
        }

        return stepCounter
    }

    private fun List<Long>.lcm(): Long {
        var result = first()

        drop(1).forEach { result = lcm(result, it) }

        return result
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_8_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_8_2_input.txt"
    }
}

private const val LEFT_STEP = "L"