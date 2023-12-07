package dev.ncovercash;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day6 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<Integer> times = Arrays
      .stream(lines.get(0).split(" "))
      .filter(s -> !s.isBlank())
      .filter(s -> Character.isDigit(s.charAt(0)))
      .map(Integer::parseInt)
      .toList();
    List<Integer> distances = Arrays
      .stream(lines.get(1).split(" "))
      .filter(s -> !s.isBlank())
      .filter(s -> Character.isDigit(s.charAt(0)))
      .map(Integer::parseInt)
      .toList();

    int product = 1;

    for (int game = 0; game < times.size(); game++) {
      int time = times.get(game);
      int distance = distances.get(game);

      // we increase this as it's the distance we need to win
      distance++;

      // we want x*(time - x) >= distance
      // x^2 - time*x + distance <= 0
      // quad formula: (time +/- sqrt(time^2 - 4*distance)) / 2
      double x1 = (time - Math.sqrt(time * time - 4 * distance)) / 2;
      double x2 = (time + Math.sqrt(time * time - 4 * distance)) / 2;

      int lowerBound = (int) Math.ceil(x1);
      int upperBound = (int) Math.floor(x2);

      int range = upperBound - lowerBound + 1;
      log.info("{}-{}, {}-{}, {}", x1, x2, lowerBound, upperBound, range);
      product *= range;
    }

    return "" + product;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    long time = Long.parseLong(
      Arrays
        .stream(lines.get(0).split(" "))
        .filter(s -> !s.isBlank())
        .filter(s -> Character.isDigit(s.charAt(0)))
        .collect(Collectors.joining())
    );
    long distance = Long.parseLong(
      Arrays
        .stream(lines.get(1).split(" "))
        .filter(s -> !s.isBlank())
        .filter(s -> Character.isDigit(s.charAt(0)))
        .collect(Collectors.joining())
    );

    // we increase this as it's the distance we need to win
    distance++;

    // we want x*(time - x) >= distance
    // x^2 - time*x + distance <= 0
    // quad formula: (time +/- sqrt(time^2 - 4*distance)) / 2
    double x1 = (time - Math.sqrt(((double) time) * time - 4d * distance)) / 2;
    double x2 = (time + Math.sqrt(((double) time) * time - 4d * distance)) / 2;

    long lowerBound = (long) Math.ceil(x1);
    long upperBound = (long) Math.floor(x2);

    long range = upperBound - lowerBound + 1;
    log.info("{}-{}, {}-{}, {}", x1, x2, lowerBound, upperBound, range);

    return "" + range;
  }
}
