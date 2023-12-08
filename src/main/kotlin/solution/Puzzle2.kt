package solution

import core.Puzzle
import java.security.InvalidParameterException

class Puzzle2(inputData: List<String>) : Puzzle(inputData) {
    private val numberRegex by lazy {
        "\\d+".toRegex()
    }

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        val possibleGames = mutableListOf<Int>()
        inputData.forEach { roundData ->
            val (gameId, gameData) = with(roundData.split(":")) {
                val gameId = numberRegex.find(first())?.value?.toInt()
                    ?: throw InvalidParameterException("Can't parse game ID from name")

                return@with gameId to drop(1).joinToString("")
            }

            if (!isGamePossible(gameData)) return@forEach

            possibleGames.add(gameId)
        }

        return possibleGames.sum()
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val cubePowerPerGame = mutableListOf<Int>()
        inputData.forEach { roundData ->
            val gameData = roundData.split(":").takeLast(1).joinToString("")

            cubePowerPerGame.add(powerSetOfMaxCubes(gameData))
        }

        return cubePowerPerGame.sum()
    }

    private fun isGamePossible(gameData: String): Boolean {
        gameData.split(";").forEach { round ->
            val roundData = round.split(",").map { it.trim() }

            roundData.forEach { color ->
                val cubeAmount = numberRegex.find(color)?.value?.toInt()
                    ?: throw InvalidParameterException("Can't parse cube amount from color data")

                val isRedCubesMoreThenMax = color.contains(RED_CUBES) && cubeAmount > MAX_RED_CUBES
                val isGreenCubesMoreThenMax = color.contains(GREEN_CUBES) && cubeAmount > MAX_GREEN_CUBES
                val isBlueCubesMoreThenMax = color.contains(BLUE_CUBES) && cubeAmount > MAX_BLUE_CUBES

                if (isRedCubesMoreThenMax || isGreenCubesMoreThenMax || isBlueCubesMoreThenMax) return false
            }
        }

        return true
    }

    private fun powerSetOfMaxCubes(gameData: String): Int {
        var maxBlueCube = 1
        var maxRedCube = 1
        var maxGreenCube = 1

        gameData.split(";").forEach { round ->
            val roundData = round.split(",").map { it.trim() }

            roundData.forEach { color ->
                val cubeAmount = numberRegex.find(color)?.value?.toInt()
                    ?: throw InvalidParameterException("Can't parse cube amount from color data")

                with(color) {
                    when {
                        contains(RED_CUBES) -> if (maxRedCube < cubeAmount) maxRedCube = cubeAmount
                        contains(GREEN_CUBES) -> if (maxGreenCube < cubeAmount) maxGreenCube = cubeAmount
                        contains(BLUE_CUBES) -> if (maxBlueCube < cubeAmount) maxBlueCube = cubeAmount
                    }
                }
            }
        }

        return maxRedCube * maxBlueCube * maxGreenCube
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_2_input.txt"
    }
}

private const val MAX_RED_CUBES = 12
private const val RED_CUBES = "red"

private const val MAX_GREEN_CUBES = 13
private const val GREEN_CUBES = "green"

private const val MAX_BLUE_CUBES = 14
private const val BLUE_CUBES = "blue"

