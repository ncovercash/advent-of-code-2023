package dev.ncovercash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

@Log4j2
public class Day18 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> instructions = InputUtils.getLines(filename);

    List<Pair<Integer, Integer>> walls = new ArrayList<>();

    int curRow = 0;
    int curCol = 0;

    for (String instruction : instructions) {
      int amt = Integer.parseInt(instruction.split(" ")[1]);

      switch (instruction.charAt(0)) {
        case 'R' -> {
          for (int i = 0; i < amt; i++) {
            curCol += 1;
            walls.add(Pair.of(curRow, curCol));
          }
        }
        case 'L' -> {
          for (int i = 0; i < amt; i++) {
            curCol -= 1;
            walls.add(Pair.of(curRow, curCol));
          }
        }
        case 'U' -> {
          for (int i = 0; i < amt; i++) {
            curRow -= 1;
            walls.add(Pair.of(curRow, curCol));
          }
        }
        case 'D' -> {
          for (int i = 0; i < amt; i++) {
            curRow += 1;
            walls.add(Pair.of(curRow, curCol));
          }
        }
      }
    }

    int minR = Integer.MAX_VALUE;
    int maxR = Integer.MIN_VALUE;
    int minC = Integer.MAX_VALUE;
    int maxC = Integer.MIN_VALUE;

    for (Pair<Integer, Integer> wall : walls) {
      minR = Math.min(minR, wall.getLeft());
      maxR = Math.max(maxR, wall.getLeft());
      minC = Math.min(minC, wall.getRight());
      maxC = Math.max(maxC, wall.getRight());
    }

    // make a border
    minR--;
    minC--;
    maxR++;
    maxC++;

    boolean[][] originalGrid = new boolean[maxR - minR + 1][maxC - minC + 1];
    boolean[][] exteriorAndEdge = new boolean[maxR - minR + 1][maxC - minC + 1];
    for (Pair<Integer, Integer> wall : walls) {
      originalGrid[wall.getLeft() - minR][wall.getRight() - minC] = true;
      exteriorAndEdge[wall.getLeft() - minR][wall.getRight() - minC] = true;
    }

    floodFill(exteriorAndEdge, 0, 0);

    boolean[][] interiorOnly = new boolean[maxR - minR + 1][maxC - minC + 1];
    for (int r = 0; r < originalGrid.length; r++) {
      for (int c = 0; c < originalGrid[0].length; c++) {
        interiorOnly[r][c] = originalGrid[r][c] || !exteriorAndEdge[r][c];
      }
    }

    log.info("Original dig path:");
    print(originalGrid);

    log.info("With exterior and edge only:");
    print(exteriorAndEdge);

    log.info("Interior only:");
    print(interiorOnly);

    int numInterior = 0;
    for (int r = 0; r < interiorOnly.length; r++) {
      for (int c = 0; c < interiorOnly[0].length; c++) {
        if (interiorOnly[r][c]) {
          numInterior++;
        }
      }
    }

    return "" + numInterior;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> instructions = InputUtils.getLines(filename);

    List<Pair<Long, Long>> corners = new ArrayList<>();

    long curRow = 0;
    long curCol = 0;

    for (String instruction : instructions) {
      String hex = instruction.split(" ")[2].substring(2, 8);
      long amt = Long.parseLong(hex.substring(0, 5), 16);

      switch (hex.charAt(5)) {
        case '0' -> {
          curCol += amt;
          corners.add(Pair.of(curRow, curCol));
        }
        case '2' -> {
          curCol -= amt;
          corners.add(Pair.of(curRow, curCol));
        }
        case '3' -> {
          curRow -= amt;
          corners.add(Pair.of(curRow, curCol));
        }
        case '1' -> {
          curRow += amt;
          corners.add(Pair.of(curRow, curCol));
        }
      }
    }

    long minR = Integer.MAX_VALUE;
    long maxR = Integer.MIN_VALUE;
    long minC = Integer.MAX_VALUE;
    long maxC = Integer.MIN_VALUE;

    for (Pair<Long, Long> corner : corners) {
      minR = Math.min(minR, corner.getLeft());
      maxR = Math.max(maxR, corner.getLeft());
      minC = Math.min(minC, corner.getRight());
      maxC = Math.max(maxC, corner.getRight());
    }

    long sum = 0;
    long perimeter = 0;
    for (int i = 0; i < corners.size(); i++) {
      Pair<Long, Long> corner1 = corners.get(i);
      Pair<Long, Long> corner2 = corners.get((i + 1) % corners.size());

      perimeter +=
        Math.abs(corner1.getLeft() - corner2.getLeft()) +
        Math.abs(corner1.getRight() - corner2.getRight());

      log.info(
        "P({},{}) -> P({},{})",
        corner1.getLeft(),
        corner1.getRight(),
        corner2.getLeft(),
        corner2.getRight()
      );

      long r1c1 = corner1.getLeft();
      long r2c1 = corner1.getRight();

      long r1c2 = corner2.getLeft();
      long r2c2 = corner2.getRight();

      long det = det(
        corner1.getLeft(),
        corner2.getRight(),
        corner1.getRight(),
        corner2.getLeft()
      );
      log.info("det([{},{};{},{}]) = {}", r1c1, r1c2, r2c1, r2c2, det);
      sum += det / 2;
      // sum +=
      //   (
      //     (corner1.getLeft() - corner2.getLeft()) *
      //     (corner1.getRight() + corner2.getRight())
      //   ) /
      //   2;
    }

    long interiorArea = Math.abs(sum);

    log.info("Perimeter: {}", perimeter);

    return "" + (interiorArea + perimeter / 2 + 1);
    // return "" + (sum);
  }

  long det(long r1c1, long r1c2, long r2c1, long r2c2) {
    return r1c1 * r2c2 - r1c2 * r2c1;
  }

  void floodFill(boolean[][] grid, int r, int c) {
    Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
    queue.add(Pair.of(r, c));

    while (!queue.isEmpty()) {
      Pair<Integer, Integer> cur = queue.poll();
      int curR = cur.getLeft();
      int curC = cur.getRight();

      if (
        curR < 0 || curR >= grid.length || curC < 0 || curC >= grid[0].length
      ) {
        continue;
      }

      if (grid[curR][curC]) {
        continue;
      }

      // filled in already
      grid[curR][curC] = true;

      queue.add(Pair.of(curR + 1, curC));
      queue.add(Pair.of(curR - 1, curC));
      queue.add(Pair.of(curR, curC + 1));
      queue.add(Pair.of(curR, curC - 1));
    }
  }

  void print(boolean[][] grid) {
    int i = 0;
    for (boolean[] row : grid) {
      String str = "";
      for (boolean cell : row) {
        str += cell ? "#" : ".";
      }
      log.info("  {} |{}", str, i++);
    }
  }
}
