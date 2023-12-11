package dev.ncovercash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

@Log4j2
public class Day11 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<Pair<Integer, Integer>> galaxies = new ArrayList<>();
    Map<Integer, Boolean> usedRows = new HashMap<>();
    Map<Integer, Boolean> usedCols = new HashMap<>();

    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);
      for (int c = 0; c < line.length(); c++) {
        if (line.charAt(c) == '#') {
          usedRows.put(r, true);
          usedCols.put(c, true);

          galaxies.add(Pair.of(r, c));
        }
      }
    }

    Map<Integer, Integer> rowSkips = new HashMap<>();
    rowSkips.put(0, 0);
    for (int r = 1; r < lines.size(); r++) {
      // row is not used
      if (!usedRows.containsKey(r)) {
        rowSkips.put(r, rowSkips.get(r - 1) + 1);
      } else {
        rowSkips.put(r, rowSkips.get(r - 1));
      }
    }

    Map<Integer, Integer> colSkips = new HashMap<>();
    colSkips.put(0, 0);
    for (int c = 1; c < lines.get(0).length(); c++) {
      // col is not used
      if (!usedCols.containsKey(c)) {
        colSkips.put(c, colSkips.get(c - 1) + 1);
      } else {
        colSkips.put(c, colSkips.get(c - 1));
      }
    }

    for (int i = 0; i < galaxies.size(); i++) {
      Pair<Integer, Integer> galaxy = galaxies.get(i);
      galaxies.set(
        i,
        Pair.of(
          galaxy.getLeft() + rowSkips.get(galaxy.getLeft()),
          galaxy.getRight() + colSkips.get(galaxy.getRight())
        )
      );
    }

    int sum = 0;

    for (int i = 0; i < galaxies.size(); i++) {
      for (int j = 0; j < i; j++) {
        Pair<Integer, Integer> galaxyA = galaxies.get(i);
        Pair<Integer, Integer> galaxyB = galaxies.get(j);

        sum +=
          Math.abs(galaxyA.getLeft() - galaxyB.getLeft()) +
          Math.abs(galaxyA.getRight() - galaxyB.getRight());
      }
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<Pair<Integer, Integer>> galaxies = new ArrayList<>();
    Map<Integer, Boolean> usedRows = new HashMap<>();
    Map<Integer, Boolean> usedCols = new HashMap<>();

    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);
      for (int c = 0; c < line.length(); c++) {
        if (line.charAt(c) == '#') {
          usedRows.put(r, true);
          usedCols.put(c, true);

          galaxies.add(Pair.of(r, c));
        }
      }
    }

    long sum = 0;

    for (int i = 0; i < galaxies.size(); i++) {
      for (int j = 0; j < i; j++) {
        Pair<Integer, Integer> galaxyA = galaxies.get(i);
        Pair<Integer, Integer> galaxyB = galaxies.get(j);

        sum +=
          Math.abs(galaxyA.getLeft() - galaxyB.getLeft()) +
          Math.abs(galaxyA.getRight() - galaxyB.getRight());

        for (
          int r = Math.min(galaxyA.getLeft(), galaxyB.getLeft()) + 1;
          r < Math.max(galaxyA.getLeft(), galaxyB.getLeft());
          r++
        ) {
          if (!usedRows.containsKey(r)) {
            sum += 1000000 - 1;
          }
        }
        for (
          int c = Math.min(galaxyA.getRight(), galaxyB.getRight()) + 1;
          c < Math.max(galaxyA.getRight(), galaxyB.getRight());
          c++
        ) {
          if (!usedCols.containsKey(c)) {
            sum += 1000000 - 1;
          }
        }
      }
    }

    return "" + sum;
  }
}
