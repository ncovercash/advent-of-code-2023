package dev.ncovercash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day3 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);

      int startingCol = -1;

      for (int c = 0; c < line.length(); c++) {
        char ch = line.charAt(c);

        if (Character.isDigit(ch) && startingCol == -1) {
          startingCol = c;
        } else if (!Character.isDigit(ch) && startingCol != -1) {
          sum += checkIsPartNumber(lines, r, startingCol, c - 1);
          startingCol = -1;
        }
      }

      if (startingCol != -1) {
        sum += checkIsPartNumber(lines, r, startingCol, line.length() - 1);
      }
    }

    return sum + "";
  }

  public int checkIsPartNumber(List<String> lines, int rBase, int c1, int c2) {
    int number = Integer.parseInt(lines.get(rBase).substring(c1, c2 + 1));

    // row above
    for (int r = rBase - 1; r <= rBase + 1; r++) {
      if (r < 0 || r >= lines.size()) {
        continue;
      }

      for (int c = c1 - 1; c <= c2 + 1; c++) {
        if (c < 0 || c >= lines.get(r).length()) {
          continue;
        }

        char ch = lines.get(r).charAt(c);

        if (ch != '.' && !Character.isDigit(ch)) {
          return number;
        }
      }
    }

    return 0;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);

      for (int c = 0; c < line.length(); c++) {
        char ch = line.charAt(c);

        if (ch == '*') {
          sum += checkGear(lines, r, c);
        }
      }
    }

    return sum + "";
  }

  public int checkGear(List<String> lines, int r, int c) {
    Map<Integer, Integer> rowAbove = numberByColumn(lines.get(r - 1));
    Map<Integer, Integer> rowCurrent = numberByColumn(lines.get(r));
    Map<Integer, Integer> rowBelow = numberByColumn(lines.get(r + 1));

    List<Integer> adjacent = new ArrayList<>();

    // row above has #.#
    if (
      rowAbove.containsKey(c - 1) &&
      !rowAbove.containsKey(c) &&
      rowAbove.containsKey(c + 1)
    ) {
      adjacent.add(rowAbove.get(c - 1));
      adjacent.add(rowAbove.get(c + 1));
    } else if (rowAbove.containsKey(c - 1)) {
      adjacent.add(rowAbove.get(c - 1));
    } else if (rowAbove.containsKey(c + 1)) {
      adjacent.add(rowAbove.get(c + 1));
    } else if (rowAbove.containsKey(c)) {
      adjacent.add(rowAbove.get(c));
    }

    // horizontal sides
    if (rowCurrent.containsKey(c - 1)) {
      adjacent.add(rowCurrent.get(c - 1));
    }
    if (rowCurrent.containsKey(c + 1)) {
      adjacent.add(rowCurrent.get(c + 1));
    }

    // row below has #.#
    if (
      rowBelow.containsKey(c - 1) &&
      !rowBelow.containsKey(c) &&
      rowBelow.containsKey(c + 1)
    ) {
      adjacent.add(rowBelow.get(c - 1));
      adjacent.add(rowBelow.get(c + 1));
    } else if (rowBelow.containsKey(c - 1)) {
      adjacent.add(rowBelow.get(c - 1));
    } else if (rowBelow.containsKey(c + 1)) {
      adjacent.add(rowBelow.get(c + 1));
    } else if (rowBelow.containsKey(c)) {
      adjacent.add(rowBelow.get(c));
    }

    log.info(
      "Gear candidate at ({}, {}) has adjacent numers: {}",
      r,
      c,
      adjacent
    );

    return adjacent.size() == 2 ? adjacent.get(0) * adjacent.get(1) : 0;
  }

  public Map<Integer, Integer> numberByColumn(String line) {
    Map<Integer, Integer> numberByColumn = new HashMap<>();

    int startIndex = -1;

    for (int c = 0; c < line.length(); c++) {
      char ch = line.charAt(c);

      if (Character.isDigit(ch) && startIndex == -1) {
        startIndex = c;
      } else if (!Character.isDigit(ch) && startIndex != -1) {
        int number = Integer.parseInt(line.substring(startIndex, c));

        for (int i = startIndex; i < c; i++) {
          numberByColumn.put(i, number);
        }

        startIndex = -1;
      }
    }

    if (startIndex != -1) {
      int number = Integer.parseInt(line.substring(startIndex));

      for (int i = startIndex; i < line.length(); i++) {
        numberByColumn.put(i, number);
      }
    }

    return numberByColumn;
  }
}
