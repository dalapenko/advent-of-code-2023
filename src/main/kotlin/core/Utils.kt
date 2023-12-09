package core

fun String.replaceAll(replaceMap: Map<String, String>): String {
    var newString = this
    replaceMap.forEach { (oldValue, newValue) ->
        newString = newString.replace(oldValue, newValue)
    }
    return newString
}

fun String.parseIntList(): List<Int> {
    val numberRegex = "\\d+".toRegex()
    return numberRegex.findAll(this)
        .map { it.value.toInt() }
        .toList()
}

fun String.parseLongList(): List<Long> {
    val numberRegex = "\\d+".toRegex()
    return numberRegex.findAll(this)
        .map { it.value.toLong() }
        .toList()
}

fun IntRange.isInclude(otherRange: IntRange): Boolean {
    return first <= otherRange.last && last >= otherRange.first
}

fun Int.decToZero(): Int {
    return dec().takeIf { it > -1 } ?: 0
}

fun Int.incWithMax(max: Int): Int {
    return inc().takeIf { it < max } ?: max
}

fun String.toOccurrencesMap(): Map<Char, Int> {
    val resultMap = mutableMapOf<Char, Int>()
    for (char in this) {
        val cValue = resultMap[char] ?: 0

        resultMap[char] = cValue + 1
    }
    return resultMap
}

fun Regex.findAllValue(input: CharSequence, startIndex: Int = 0): Sequence<String> {
    return findAll(input, startIndex).map(MatchResult::value)
}

/**
 * Наименьшее общее кратное
 */
fun lcm(a: Long, b: Long): Long {
    return (a * b) / gcd(a, b)
}

/**
 * Наибольший общий делитель
 */
fun gcd(a: Long, b: Long): Long {
    var n1 = a
    var n2 = b

    while (n2 != 0L) {
        n1 = n2.also { n2 = n1 % n2 }
    }

    return n1
}