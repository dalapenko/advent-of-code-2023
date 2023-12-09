package solution

import core.Puzzle
import core.parseLongList

typealias TransmogrificationMap = List<Pair<LongRange, LongRange>>

class Puzzle5(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        val (seeds, maps) = with(inputData) {
            first().parseLongList() to drop(2).joinToString(SINGLE_BREAK_LINE).split(DOUBLE_BREAK_LINE)
        }

        val almanac = Almanac(maps)

        return seeds.map { seed ->
            val soil = almanac.seedToSoilMap.getDestinationBySource(seed)
            val fertilizer = almanac.soilToFertilizerMap.getDestinationBySource(soil)
            val water = almanac.fertilizerToWaterMap.getDestinationBySource(fertilizer)
            val light = almanac.waterToLightMap.getDestinationBySource(water)
            val temperature = almanac.lightToTemperatureMap.getDestinationBySource(light)
            val humidity = almanac.temperatureToHumidityMap.getDestinationBySource(temperature)

            return@map almanac.humidityToLocationMap.getDestinationBySource(humidity)
        }.min()
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val (seeds, maps) = with(inputData) {
            first().parseLongList().chunked(2).map {
                it.first()..<it.first() + it.last()
            }.sortedBy { it.first } to drop(2).joinToString(SINGLE_BREAK_LINE).split(DOUBLE_BREAK_LINE)
        }

        val almanac = Almanac(maps)

        val soils = almanac.seedToSoilMap.getRangesOccurrences(seeds)
        val fertilizer = almanac.soilToFertilizerMap.getRangesOccurrences(soils)
        val water = almanac.fertilizerToWaterMap.getRangesOccurrences(fertilizer)
        val light = almanac.waterToLightMap.getRangesOccurrences(water)
        val temperature = almanac.lightToTemperatureMap.getRangesOccurrences(light)
        val humidity = almanac.temperatureToHumidityMap.getRangesOccurrences(temperature)

        return almanac.humidityToLocationMap.getRangesOccurrences(humidity).minBy(LongRange::first).first
    }

    private fun TransmogrificationMap.getDestinationBySource(value: Long): Long {
        val (sourceRange, destinationRange) = find { it.first.contains(value) } ?: return value

        val range = value - sourceRange.first

        return destinationRange.first + range
    }

    private fun TransmogrificationMap.getRangesOccurrences(sourceRanges: List<LongRange>): List<LongRange> {
        return rangesOccurrencesList(sourceRanges, this)
    }

    private fun rangesOccurrencesList(
        sourceRange: List<LongRange>,
        destinationRangeMap: List<Pair<LongRange, LongRange>>
    ): List<LongRange> {
        return sourceRange.map { range ->
            val occurrencesList = mutableListOf<LongRange>()

            var srcRangeStart = range.first
            val srcRangedEnd = range.last

            while (srcRangeStart < srcRangedEnd) {
                val (srcMap, dstMap) = destinationRangeMap.find { (src, _) ->
                    src.contains(srcRangeStart)
                } ?: (null to null)

                if (srcMap == null || dstMap == null) {
                    occurrencesList.add(srcRangeStart..srcRangedEnd)
                    break
                }

                val dstRangeStart = dstMap.first + (srcRangeStart - srcMap.first)
                val dstRangeEnd = if (srcMap.last <= srcRangedEnd) {
                    srcRangeStart = srcMap.last + 1
                    dstMap.last
                } else {
                    srcRangeStart = srcRangedEnd
                    dstMap.last - (srcMap.last - srcRangedEnd)
                }

                occurrencesList.add(dstRangeStart..dstRangeEnd)
            }

            return@map occurrencesList
        }.flatten()
    }

    private fun createSourceToDestinationMap(
        inputData: List<String>
    ): TransmogrificationMap {
        return inputData.map { data ->
            val (destinationStart, sourceStart, range) = with(data.parseLongList()) {
                Triple(first(), drop(1).first(), last())
            }

            sourceStart..<sourceStart + range to destinationStart..<destinationStart + range
        }
    }

    private class Almanac(
        transmogrificationMap: List<String>
    ) {
        var seedToSoilMap: TransmogrificationMap = emptyList()
            private set
        var soilToFertilizerMap: TransmogrificationMap = emptyList()
            private set
        var fertilizerToWaterMap: TransmogrificationMap = emptyList()
            private set
        var waterToLightMap: TransmogrificationMap = emptyList()
            private set
        var lightToTemperatureMap: TransmogrificationMap = emptyList()
            private set
        var temperatureToHumidityMap: TransmogrificationMap = emptyList()
            private set
        var humidityToLocationMap: TransmogrificationMap = emptyList()
            private set

        init {
            transmogrificationMap.forEach { mapData ->
                val (name, data) = with(mapData.split(SINGLE_BREAK_LINE)) {
                    first() to drop(1)
                }
                with(name) {
                    when {
                        contains(SEED_TO_SOIL) ->
                            seedToSoilMap = createSourceToDestinationMap(data)

                        contains(SOIL_TO_FERTILIZER) ->
                            soilToFertilizerMap = createSourceToDestinationMap(data)

                        contains(FERTILIZER_TO_WATER) ->
                            fertilizerToWaterMap = createSourceToDestinationMap(data)

                        contains(WATER_TO_LIGHT) ->
                            waterToLightMap = createSourceToDestinationMap(data)

                        contains(LIGHT_TO_TEMPERATURE) ->
                            lightToTemperatureMap = createSourceToDestinationMap(data)

                        contains(TEMPERATURE_TO_HUMIDITY) ->
                            temperatureToHumidityMap = createSourceToDestinationMap(data)

                        contains(HUMIDITY_TO_LOCATION) ->
                            humidityToLocationMap = createSourceToDestinationMap(data)
                    }
                }
            }
        }

        private fun createSourceToDestinationMap(
            inputData: List<String>
        ): TransmogrificationMap {
            return inputData.map { data ->
                val (destinationStart, sourceStart, range) = with(data.parseLongList()) {
                    Triple(first(), drop(1).first(), last())
                }

                sourceStart..<sourceStart + range to destinationStart..<destinationStart + range
            }
        }
    }

    companion object {
        const val INPUT_FILE_NAME = "puzzle_5_input.txt"
        const val TEST_INPUT_FILE_NAME = "testdata/puzzle_5_input.txt"
    }
}


private const val SEED_TO_SOIL = "seed-to-soil"
private const val SOIL_TO_FERTILIZER = "soil-to-fertilizer"
private const val FERTILIZER_TO_WATER = "fertilizer-to-water"
private const val WATER_TO_LIGHT = "water-to-light"
private const val LIGHT_TO_TEMPERATURE = "light-to-temperature"
private const val TEMPERATURE_TO_HUMIDITY = "temperature-to-humidity"
private const val HUMIDITY_TO_LOCATION = "humidity-to-location"

private const val SINGLE_BREAK_LINE = "\n"
private const val DOUBLE_BREAK_LINE = "\n\n"