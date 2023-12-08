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