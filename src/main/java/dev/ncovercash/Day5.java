package dev.ncovercash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day5 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<String>> clumps = InputUtils.getLinesByClump(filename);

    List<Long> neededSeeds = Arrays
      .stream(clumps.get(0).get(0).split(": ")[1].split(" "))
      .map(String::trim)
      .filter(str -> !str.isBlank())
      .map(Long::parseLong)
      .toList();

    List<Range> seedToSoil = makeRangeListFromAlmanac(clumps.get(1));
    List<Range> soilToFertilizer = makeRangeListFromAlmanac(clumps.get(2));
    List<Range> fertilizerToWater = makeRangeListFromAlmanac(clumps.get(3));
    List<Range> waterToLight = makeRangeListFromAlmanac(clumps.get(4));
    List<Range> lightToTemperature = makeRangeListFromAlmanac(clumps.get(5));
    List<Range> temperatureToHumidity = makeRangeListFromAlmanac(clumps.get(6));
    List<Range> humidityToLocation = makeRangeListFromAlmanac(clumps.get(7));

    log.info("Built maps");

    long smallestLocation = Long.MAX_VALUE;

    for (long seed : neededSeeds) {
      long soil = getCorresponding(seedToSoil, seed);
      long fertilizer = getCorresponding(soilToFertilizer, soil);
      long water = getCorresponding(fertilizerToWater, fertilizer);
      long light = getCorresponding(waterToLight, water);
      long temperature = getCorresponding(lightToTemperature, light);
      long humidity = getCorresponding(temperatureToHumidity, temperature);
      long location = getCorresponding(humidityToLocation, humidity);

      if (location < smallestLocation) {
        smallestLocation = location;
      }
    }

    return "" + smallestLocation;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<List<String>> clumps = InputUtils.getLinesByClump(filename);

    List<Long> neededSeeds = Arrays
      .stream(clumps.get(0).get(0).split(": ")[1].split(" "))
      .map(String::trim)
      .filter(str -> !str.isBlank())
      .map(Long::parseLong)
      .toList();

    List<Range> seedToSoil = makeRangeListFromAlmanac(clumps.get(1));
    List<Range> soilToFertilizer = makeRangeListFromAlmanac(clumps.get(2));
    List<Range> fertilizerToWater = makeRangeListFromAlmanac(clumps.get(3));
    List<Range> waterToLight = makeRangeListFromAlmanac(clumps.get(4));
    List<Range> lightToTemperature = makeRangeListFromAlmanac(clumps.get(5));
    List<Range> temperatureToHumidity = makeRangeListFromAlmanac(clumps.get(6));
    List<Range> humidityToLocation = makeRangeListFromAlmanac(clumps.get(7));

    log.info("Built maps");

    long smallestLocation = Long.MAX_VALUE;

    for (int i = 0; i < neededSeeds.size(); i += 2) {
      log.info("Starting seed {}", i);
      long firstSeed = neededSeeds.get(i);
      long lastSeed = neededSeeds.get(i) + neededSeeds.get(i + 1) - 1;

      for (long seed = firstSeed; seed <= lastSeed; seed++) {
        long wiggleRoom = Long.MAX_VALUE;

        long soil = getCorresponding(seedToSoil, seed);
        wiggleRoom = Math.min(wiggleRoom, getWiggleRoom(seedToSoil, seed));
        long fertilizer = getCorresponding(soilToFertilizer, soil);
        wiggleRoom =
          Math.min(wiggleRoom, getWiggleRoom(soilToFertilizer, soil));
        long water = getCorresponding(fertilizerToWater, fertilizer);
        wiggleRoom =
          Math.min(wiggleRoom, getWiggleRoom(fertilizerToWater, fertilizer));
        long light = getCorresponding(waterToLight, water);
        wiggleRoom = Math.min(wiggleRoom, getWiggleRoom(waterToLight, water));
        long temperature = getCorresponding(lightToTemperature, light);
        wiggleRoom =
          Math.min(wiggleRoom, getWiggleRoom(lightToTemperature, light));
        long humidity = getCorresponding(temperatureToHumidity, temperature);
        wiggleRoom =
          Math.min(
            wiggleRoom,
            getWiggleRoom(temperatureToHumidity, temperature)
          );
        long location = getCorresponding(humidityToLocation, humidity);
        wiggleRoom =
          Math.min(wiggleRoom, getWiggleRoom(humidityToLocation, humidity));

        if (wiggleRoom > 0) {
          seed += wiggleRoom - 1;
          log.info("Nyooming by {}", wiggleRoom);
        }

        if (location < smallestLocation) {
          smallestLocation = location;
        }
      }
    }

    return "" + smallestLocation;
  }

  private static List<Range> makeRangeListFromAlmanac(List<String> rules) {
    List<Range> ranges = new ArrayList<>();
    for (String line : rules.subList(1, rules.size())) {
      String[] pieces = line.split(" ");
      long dest = Long.parseLong(pieces[0]);
      long src = Long.parseLong(pieces[1]);
      long num = Integer.parseInt(pieces[2]);

      ranges.add(new Range(src, src + num - 1, dest));
    }
    return ranges;
  }

  private static long getCorresponding(List<Range> ranges, long num) {
    return ranges
      .stream()
      .filter(r -> r.contains(num))
      .findFirst()
      .map(r -> r.get(num))
      .orElse(num);
  }

  private static long getWiggleRoom(List<Range> ranges, long num) {
    return ranges
      .stream()
      .filter(r -> r.contains(num))
      .findFirst()
      .map(r -> r.max() - num)
      .orElseGet(() ->
        ranges
          .stream()
          .sorted((a, b) -> (a.min() - b.min()) < 0 ? -1 : 1)
          .filter(r -> r.min() > num)
          .findFirst()
          .map(r -> r.min() - num)
          .orElse(Long.MAX_VALUE)
      );
  }

  record Range(long min, long max, long basis) {
    boolean contains(long num) {
      return num >= min && num <= max;
    }

    long get(long num) {
      return num - min + basis;
    }
  }
}
