package solution

import core.Puzzle
import core.parseIntList
import core.parseLongList

class Puzzle6(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        val raceData = with(inputData) {
            val times = first().parseLongList()
            val distance = last().parseLongList()

            times.zip(distance)
        }

        return raceData.map { (time, distance) ->
            val winRange = (0..time).searchRange { sec ->
                (time - sec) * sec > distance
            }

            winRange.count()
        }.reduce { acc, i -> acc * i }
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val (time, distance) = with(inputData) {
            first().parseIntList().joinToString(NO_SPACE).toLong() to last().parseIntList().joinToString(NO_SPACE)
                .toLong()
        }

        return (0..time).searchRange { sec ->
            (time - sec) * sec > distance
        }.count()
    }

    private fun LongRange.searchRange(condition: (Long) -> Boolean): LongRange {
        val middle = (last + first) / 2
        var (left, leftRight) = first to middle
        var (rightLeft, right) = middle to last

        val searchLeftRange: (Long) -> Unit = LeftSearch@{
            if (left > leftRight) return@LeftSearch

            if (!condition(it)) left = it + 1 else leftRight = it - 1
        }

        val searchRightRange: (Long) -> Unit = RightSearch@{
            if (rightLeft > right) return@RightSearch

            if (!condition(it)) right = it - 1 else rightLeft = it + 1
        }

        while (left <= leftRight && rightLeft <= right) {
            val leftMiddle = (left + leftRight) / 2
            val rightMiddle = (rightLeft + right) / 2

            searchLeftRange(leftMiddle)
            searchRightRange(rightMiddle)
        }

        return leftRight + 1..right
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_6_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_6_input.txt"
    }
}

private const val SPACE = " "
private const val NO_SPACE = ""