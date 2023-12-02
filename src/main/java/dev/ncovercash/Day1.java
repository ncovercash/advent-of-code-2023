package dev.ncovercash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day1 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    return (
      lines
        .stream()
        .map(line -> line.chars().filter(Character::isDigit).toArray())
        .mapToInt(numbers ->
          (numbers[0] - '0') * 10 + (numbers[numbers.length - 1] - '0')
        )
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
        .mapToInt(line -> getFirstNum(line) * 10 + getLastNum(line))
        .sum() +
      ""
    );
  }

  static Map<String, Integer> STRING_NUM_MAP = new HashMap<>();

  static {
    STRING_NUM_MAP.put("0", 0);
    STRING_NUM_MAP.put("1", 1);
    STRING_NUM_MAP.put("2", 2);
    STRING_NUM_MAP.put("3", 3);
    STRING_NUM_MAP.put("4", 4);
    STRING_NUM_MAP.put("5", 5);
    STRING_NUM_MAP.put("6", 6);
    STRING_NUM_MAP.put("7", 7);
    STRING_NUM_MAP.put("8", 8);
    STRING_NUM_MAP.put("9", 9);

    STRING_NUM_MAP.put("one", 1);
    STRING_NUM_MAP.put("two", 2);
    STRING_NUM_MAP.put("three", 3);
    STRING_NUM_MAP.put("four", 4);
    STRING_NUM_MAP.put("five", 5);
    STRING_NUM_MAP.put("six", 6);
    STRING_NUM_MAP.put("seven", 7);
    STRING_NUM_MAP.put("eight", 8);
    STRING_NUM_MAP.put("nine", 9);
  }

  private static int getFirstNum(String line) {
    int bestIndex = Integer.MAX_VALUE;
    int bestNum = -1;

    for (Map.Entry<String, Integer> entry : STRING_NUM_MAP.entrySet()) {
      // if the key is in the line
      if (
        line.indexOf(entry.getKey()) != -1 &&
        line.indexOf(entry.getKey()) < bestIndex
      ) {
        bestIndex = line.indexOf(entry.getKey());
        bestNum = entry.getValue();
      }
    }

    return bestNum;
  }

  private static int getLastNum(String line) {
    int bestIndex = Integer.MIN_VALUE;
    int bestNum = -1;

    for (Map.Entry<String, Integer> entry : STRING_NUM_MAP.entrySet()) {
      // if the key is in the line
      if (
        line.lastIndexOf(entry.getKey()) != -1 &&
        line.lastIndexOf(entry.getKey()) > bestIndex
      ) {
        bestIndex = line.lastIndexOf(entry.getKey());
        bestNum = entry.getValue();
      }
    }

    return bestNum;
  }
}
