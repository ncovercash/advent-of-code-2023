package dev.ncovercash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day2 implements Solution {

  private static final Map<String, Integer> MAX_CUBES = Map.of(
    "red",
    12,
    "green",
    13,
    "blue",
    14
  );

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    return (
      lines
        .stream()
        .mapToInt(line -> {
          // Game ##: X blue, X red; ...
          String[] outerSplit = line.split(": ");

          int gameNum = Integer.parseInt(outerSplit[0].split(" ")[1]);

          String[] games = outerSplit[1].split("; ");

          for (String game : games) {
            String[] cubeCounts = game.split(", ");

            for (String cubeCount : cubeCounts) {
              String[] colorAndCount = cubeCount.split(" ");

              String color = colorAndCount[1];
              int count = Integer.parseInt(colorAndCount[0]);

              if (count > MAX_CUBES.get(color)) {
                // can't happen; we only want possible games
                return 0;
              }
            }
          }

          return gameNum;
        })
        .sum() +
      ""
    );
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    return (
      lines
        .stream()
        .mapToInt(line -> {
          // Game ##: X blue, X red; ...
          String[] outerSplit = line.split(": ");

          int gameNum = Integer.parseInt(outerSplit[0].split(" ")[1]);

          Map<String, Integer> minCounts = new HashMap<>();

          String[] games = outerSplit[1].split("; ");

          for (String game : games) {
            String[] cubeCounts = game.split(", ");

            for (String cubeCount : cubeCounts) {
              String[] colorAndCount = cubeCount.split(" ");

              String color = colorAndCount[1];
              int count = Integer.parseInt(colorAndCount[0]);

              minCounts.put(
                color,
                Math.max(count, minCounts.getOrDefault(color, 0))
              );
            }
          }

          return minCounts
            .values()
            .stream()
            .mapToInt(Integer::intValue)
            .reduce((a, b) -> a * b)
            .getAsInt();
        })
        .sum() +
      ""
    );
  }
}
