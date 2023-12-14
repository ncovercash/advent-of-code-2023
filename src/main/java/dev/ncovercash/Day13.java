package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day13 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<String>> patterns = InputUtils.getLinesByClump(filename);

    int rowSum = 0;
    int colSum = 0;

    for (List<String> pattern : patterns) {
      List<List<Character>> grid = new ArrayList<>();
      for (String row : pattern) {
        grid.add(row.chars().mapToObj(c -> (char) c).toList());
      }

      String reflection = getReflection(grid);
      if (reflection == null) {
        log.error("No reflection found for {}", pattern);
      } else if (reflection.startsWith("R")) {
        rowSum += Integer.parseInt(reflection.substring(1));
      } else if (reflection.startsWith("C")) {
        colSum += Integer.parseInt(reflection.substring(1));
      }
    }

    return "" + (rowSum * 100 + colSum);
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<List<String>> patterns = InputUtils.getLinesByClump(filename);

    int rowSum = 0;
    int colSum = 0;

    for (List<String> pattern : patterns) {
      List<List<Character>> grid = new ArrayList<>();
      for (String row : pattern) {
        grid.add(
          row
            .chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toCollection(ArrayList::new))
        );
      }

      String originalReflection = getReflection(grid);
      boolean found = false;

      for (int r = 0; r < grid.size() && !found; r++) {
        for (int c = 0; c < grid.get(0).size() && !found; c++) {
          if (grid.get(r).get(c).equals('.')) {
            grid.get(r).set(c, '#');
          } else {
            grid.get(r).set(c, '.');
          }

          String reflection = getReflection(grid, originalReflection);

          if (reflection != null && !reflection.equals(originalReflection)) {
            if (reflection.startsWith("R")) {
              rowSum += Integer.parseInt(reflection.substring(1));
            } else if (reflection.startsWith("C")) {
              colSum += Integer.parseInt(reflection.substring(1));
            }
            found = true;
          }

          // put it back
          if (grid.get(r).get(c).equals('.')) {
            grid.get(r).set(c, '#');
          } else {
            grid.get(r).set(c, '.');
          }
        }
      }

      if (!found) {
        log.error("No reflection found for {}", pattern);
      }
    }

    return "" + (rowSum * 100 + colSum);
  }

  String getReflection(List<List<Character>> grid) {
    return getReflection(grid, null);
  }

  String getReflection(List<List<Character>> grid, String skip) {
    for (int r = 0; r < grid.size() - 1; r++) {
      if (("R" + (r + 1)).equals(skip)) {
        continue;
      }
      if (rowReflects(grid, r, r + 1)) {
        return "R" + (r + 1);
      }
    }

    for (int c = 0; c < grid.get(0).size() - 1; c++) {
      if (("C" + (c + 1)).equals(skip)) {
        continue;
      }
      if (colReflects(grid, c, c + 1)) {
        return "C" + (c + 1);
      }
    }

    return null;
  }

  boolean rowReflects(List<List<Character>> grid, int r1, int r2) {
    for (
      int offset = 0;
      r1 - offset >= 0 && r2 + offset < grid.size();
      offset++
    ) {
      if (!rowEquals(grid, r1 - offset, r2 + offset)) {
        return false;
      }
    }
    return true;
  }

  boolean rowEquals(List<List<Character>> grid, int r1, int r2) {
    for (int c = 0; c < grid.get(0).size(); c++) {
      if (!grid.get(r1).get(c).equals(grid.get(r2).get(c))) {
        return false;
      }
    }
    return true;
  }

  boolean colReflects(List<List<Character>> grid, int c1, int c2) {
    for (
      int offset = 0;
      c1 - offset >= 0 && c2 + offset < grid.get(0).size();
      offset++
    ) {
      if (!colEquals(grid, c1 - offset, c2 + offset)) {
        return false;
      }
    }
    return true;
  }

  boolean colEquals(List<List<Character>> grid, int c1, int c2) {
    for (int r = 0; r < grid.size(); r++) {
      if (!grid.get(r).get(c1).equals(grid.get(r).get(c2))) {
        return false;
      }
    }
    return true;
  }
}
