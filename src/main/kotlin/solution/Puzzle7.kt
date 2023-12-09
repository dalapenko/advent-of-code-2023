package solution

import core.Puzzle
import core.toOccurrencesMap

typealias CardInHand = MutableList<Pair<String, Long>>
typealias WeightMap = Map<Char, Int>

class Puzzle7(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        val gameData = inputData.map { with(it.split(SPACE)) { first() to last().toLong() } }

        val handMap = sortedMapOf<HandType, CardInHand>(HandType.comparator)

        gameData.forEach { (hand, bid) ->
            val handType = HandType.getHandType(hand)

            handMap.getOrPut(handType) { mutableListOf() }
                .add(hand to bid)
        }

        val cardWeightMap = mapOf(
            'A' to 14, 'K' to 13, 'Q' to 12, 'J' to 11, 'T' to 10, '9' to 9,
            '8' to 8, '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2
        )

        val sortedHands = handMap.values.sortByWeight(cardWeightMap)

        return sortedHands.map { it.second }.reduceIndexed { index, acc, bid ->
            return@reduceIndexed acc + bid * (index + 1)
        }
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val gameData = inputData.map { with(it.split(SPACE)) { first() to last().toLong() } }

        val handMap = sortedMapOf<HandType, CardInHand>(HandType.comparator)

        gameData.forEach { (hand, bid) ->
            val handType = HandType.getHandType(hand, withJokerRule = true)

            handMap.getOrPut(handType) { mutableListOf() }
                .add(hand to bid)
        }

        val cardWeightMap = mapOf(
            'A' to 13, 'K' to 12, 'Q' to 11, 'T' to 10, '9' to 9, '8' to 8,
            '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2, 'J' to 1
        )

        val sortedHands = handMap.values.sortByWeight(cardWeightMap)

        return sortedHands.map { it.second }.reduceIndexed { index, acc, bid ->
            return@reduceIndexed acc + bid * (index + 1)
        }
    }

    private fun Collection<CardInHand>.sortByWeight(weightMap: WeightMap): List<Pair<String, Long>> {
        return map { handInType ->
            if (handInType.size == 1) return@map handInType

            return@map handInType.sortedWith(HandComparator(weightMap))
        }.flatten()
    }

    private class HandComparator(
        private val weightMap: WeightMap
    ) : Comparator<Pair<String, Long>> {
        override fun compare(o1: Pair<String, Long>, o2: Pair<String, Long>): Int {
            if (o1.first == o2.first) return 0

            val charMap = o1.first.zip(o2.first)

            charMap.forEach { (first, second) ->
                if (first == second) return@forEach

                return (weightMap[first] ?: 0) - (weightMap[second] ?: 0)
            }

            return 0
        }

    }

    private enum class HandType(val weight: Int) {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1);

        companion object {
            val comparator = Comparator<HandType> { o1, o2 -> return@Comparator o1.weight - o2.weight }

            fun getHandType(input: String, withJokerRule: Boolean = false): HandType {
                val occurrencesMap = getOccurrencesMap(input, withJokerRule)

                val occurrences = occurrencesMap.size
                if (occurrences == 1) return FIVE_OF_A_KIND

                val highestOccurrences = occurrencesMap.values.maxOf { it }

                return when (occurrences) {
                    2 -> if (highestOccurrences == 4) FOUR_OF_A_KIND else FULL_HOUSE
                    3 -> if (highestOccurrences == 3) THREE_OF_KIND else TWO_PAIR
                    4 -> if (highestOccurrences == 2) ONE_PAIR else HIGH_CARD
                    else -> HIGH_CARD
                }
            }

            private fun getOccurrencesMap(input: String, withJokerRule: Boolean = false): Map<Char, Int> {
                val occurrencesMap = input.toOccurrencesMap()
                if (!withJokerRule || !input.contains(JOKER)) return occurrencesMap

                val occurrencesMapWithoutJoker = input.toOccurrencesMap().filter { it.key != JOKER }

                if (occurrencesMapWithoutJoker.isEmpty()) return occurrencesMap

                val highestOccurrencesCardWithoutJoker = occurrencesMapWithoutJoker.maxBy { it.value }.key

                return input.replace(JOKER, highestOccurrencesCardWithoutJoker).toOccurrencesMap()
            }
        }
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_7_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_7_input.txt"
    }
}

private const val SPACE = " "
private const val JOKER = 'J'