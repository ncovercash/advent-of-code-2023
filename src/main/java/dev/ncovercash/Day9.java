package dev.ncovercash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day9 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (String line : lines) {
      List<Integer> history = Arrays
        .stream(line.split(" "))
        .map(Integer::parseInt)
        .collect(Collectors.toList());

      List<List<Integer>> histories = new ArrayList<>();
      histories.add(history);

      while (true) {
        List<Integer> differences = getDifferences(
          histories.get(histories.size() - 1)
        );

        histories.add(differences);

        if (allZeroes(differences)) {
          break;
        }
      }

      // add zero to bottom row
      histories.get(histories.size() - 1).add(0);

      for (int i = histories.size() - 2; i >= 0; i--) {
        List<Integer> row = histories.get(i);
        List<Integer> belowRow = histories.get(i + 1);

        row.add(row.get(row.size() - 1) + belowRow.get(belowRow.size() - 1));
      }

      sum += histories.get(0).get(histories.get(0).size() - 1);
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (String line : lines) {
      List<Integer> history = Arrays
        .stream(line.split(" "))
        .map(Integer::parseInt)
        .collect(Collectors.toList());

      List<List<Integer>> histories = new ArrayList<>();
      histories.add(history);

      while (true) {
        List<Integer> differences = getDifferences(
          histories.get(histories.size() - 1)
        );

        histories.add(differences);

        if (allZeroes(differences)) {
          break;
        }
      }

      // add zero to bottom row
      histories.get(histories.size() - 1).add(0);

      for (int i = histories.size() - 2; i >= 0; i--) {
        List<Integer> row = histories.get(i);
        List<Integer> belowRow = histories.get(i + 1);

        row.add(0, row.get(0) - belowRow.get(0));
      }

      sum += histories.get(0).get(0);
    }

    return "" + sum;
  }

  public static List<Integer> getDifferences(List<Integer> history) {
    List<Integer> differences = new ArrayList<>();

    for (int i = 0; i < history.size() - 1; i++) {
      differences.add(history.get(i + 1) - history.get(i));
    }

    return differences;
  }

  public static boolean allZeroes(List<Integer> history) {
    return history.stream().allMatch(i -> i == 0);
  }
}
