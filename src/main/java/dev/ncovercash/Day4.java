package dev.ncovercash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day4 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (String game : lines) {
      String importantPart = game.split(": ")[1];
      String winningList = importantPart.split(" \\| ")[0];
      String drawnList = importantPart.split(" \\| ")[1];

      log.info("{}, {}", winningList, drawnList);

      List<Integer> winning = Arrays
        .stream(winningList.split(" "))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .map(Integer::parseInt)
        .toList();
      List<Integer> drawn = Arrays
        .stream(drawnList.split(" "))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .map(Integer::parseInt)
        .toList();

      int matching = (int) drawn.stream().filter(winning::contains).count();
      log.info("{}, {} = {}", winning, drawn, matching);

      if (matching > 0) {
        sum += Math.pow(2, matching - 1);
      }
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    Map<Integer, Integer> multiplier = new HashMap<>();

    for (int i = 0; i < lines.size(); i++) {
      String game = lines.get(i);

      String importantPart = game.split(": ")[1];
      String winningList = importantPart.split(" \\| ")[0];
      String drawnList = importantPart.split(" \\| ")[1];

      List<Integer> winning = Arrays
        .stream(winningList.split(" "))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .map(Integer::parseInt)
        .toList();
      List<Integer> drawn = Arrays
        .stream(drawnList.split(" "))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .map(Integer::parseInt)
        .toList();

      int matching = (int) drawn.stream().filter(winning::contains).count();
      log.info("{}, {} = {}", winning, drawn, matching);

      for (int j = i + 1; j <= i + matching; j++) {
        multiplier.put(
          j,
          multiplier.getOrDefault(j, 1) + multiplier.getOrDefault(i, 1)
        );
        log.info("Card {} has multiplier {}", j + 1, multiplier.get(j));
      }
    }

    int sum = 0;

    for (int i = 0; i < lines.size(); i++) {
      sum += multiplier.getOrDefault(i, 1);
    }

    return "" + sum;
  }
}
