package solution

import core.Puzzle
import core.parseLongList

typealias AlmanacMap = List<Pair<LongRange, LongRange>>

class Puzzle5(inputData: List<String>) : Puzzle(inputData) {

    override fun firstPuzzleSolution(inputData: List<String>): Any {
        val (seeds, maps) = with(inputData) {
            first().parseLongList() to drop(2).joinToString(SINGLE_BREAK_LINE).split(DOUBLE_BREAK_LINE)
        }

        var seedToSoilMap: AlmanacMap = emptyList()
        var soilToFertilizerMap: AlmanacMap = emptyList()
        var fertilizerToWaterMap: AlmanacMap = emptyList()
        var waterToLightMap: AlmanacMap = emptyList()
        var lightToTemperatureMap: AlmanacMap = emptyList()
        var temperatureToHumidityMap: AlmanacMap = emptyList()
        var humidityToLocationMap: AlmanacMap = emptyList()

        maps.forEach { mapData ->
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

        return seeds.map { seed ->
            val soil = seedToSoilMap.getDestinationBySource(seed)
            val fertilizer = soilToFertilizerMap.getDestinationBySource(soil)
            val water = fertilizerToWaterMap.getDestinationBySource(fertilizer)
            val light = waterToLightMap.getDestinationBySource(water)
            val temperature = lightToTemperatureMap.getDestinationBySource(light)
            val humidity = temperatureToHumidityMap.getDestinationBySource(temperature)

            return@map humidityToLocationMap.getDestinationBySource(humidity)
        }.min()
    }

    override fun secondPuzzleSolution(inputData: List<String>): Any {
        val (seeds, maps) = with(inputData) {
            first().parseLongList().chunked(2).map {
                it.first()..<it.first() + it.last()
            }.sortedBy { it.first } to drop(2).joinToString(SINGLE_BREAK_LINE).split(DOUBLE_BREAK_LINE)
        }

        var seedToSoilMap: AlmanacMap = emptyList()
        var soilToFertilizerMap: AlmanacMap = emptyList()
        var fertilizerToWaterMap: AlmanacMap = emptyList()
        var waterToLightMap: AlmanacMap = emptyList()
        var lightToTemperatureMap: AlmanacMap = emptyList()
        var temperatureToHumidityMap: AlmanacMap = emptyList()
        var humidityToLocationMap: AlmanacMap = emptyList()

        maps.forEach { mapData ->
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

        val soils = seedToSoilMap.getRangesOccurrences(seeds)
        val fertilizer = soilToFertilizerMap.getRangesOccurrences(soils)
        val water = fertilizerToWaterMap.getRangesOccurrences(fertilizer)
        val light = waterToLightMap.getRangesOccurrences(water)
        val temperature = lightToTemperatureMap.getRangesOccurrences(light)
        val humidity = temperatureToHumidityMap.getRangesOccurrences(temperature)

        return humidityToLocationMap.getRangesOccurrences(humidity).minBy(LongRange::first).first
    }

    private fun AlmanacMap.getDestinationBySource(value: Long): Long {
        val (sourceRange, destinationRange) = find { it.first.contains(value) } ?: return value

        val range = value - sourceRange.first

        return destinationRange.first + range
    }

    private fun AlmanacMap.getRangesOccurrences(sourceRanges: List<LongRange>): List<LongRange> {
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
    ): AlmanacMap {
        return inputData.map { data ->
            val (destinationStart, sourceStart, range) = with(data.parseLongList()) {
                Triple(first(), drop(1).first(), last())
            }

            sourceStart..<sourceStart + range to destinationStart..<destinationStart + range
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