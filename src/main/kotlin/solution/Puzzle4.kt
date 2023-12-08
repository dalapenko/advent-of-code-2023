package solution

import core.Puzzle
import core.parseIntList

class Puzzle4(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        var totalScore = 0
        inputData.forEach { cardData ->
            val (winNumbers, ticketNumbers) = parseNumbersFromCardData(cardData)

            var ticketScore = 0

            winNumbers.forEach CheckTicket@{ num ->
                if (!ticketNumbers.contains(num)) return@CheckTicket

                if (ticketScore == 0) ticketScore++ else ticketScore *= 2
            }

            totalScore += ticketScore
        }

        return totalScore
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val cardAmountMap = mutableMapOf<Int, Int>()

        inputData.forEachIndexed { cardNumber, cardData ->
            val currentAmountOfCard = cardAmountMap.getOrPut(cardNumber) { 1 }
            val (winNumbers, ticketNumbers) = parseNumbersFromCardData(cardData)

            winNumbers
                .filter { num -> ticketNumbers.contains(num) }
                .forEachIndexed { index, _ ->
                    val serialNumber = index + 1
                    val nextCardNumber = cardNumber + serialNumber
                    val currentAmountOfNextCard = cardAmountMap.getOrPut(nextCardNumber) { 1 }

                    cardAmountMap[nextCardNumber] = currentAmountOfNextCard + currentAmountOfCard
                }
        }

        return cardAmountMap.values.sum()
    }

    private fun parseNumbersFromCardData(
        cardData: String
    ): Pair<List<Int>, List<Int>> {
        val gameData = cardData.split(":").last()
        return with(gameData.split("|")) {
            first().parseIntList() to last().parseIntList()
        }
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_4_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_4_input.txt"
    }
}

