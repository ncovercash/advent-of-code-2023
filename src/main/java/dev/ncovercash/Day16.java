package dev.ncovercash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.util.Pair;

@Log4j2
public class Day16 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<Character>> lines = InputUtils.getCharacterArray(filename);
    List<List<Tile>> map = new ArrayList<>();

    for (int r = 0; r < lines.size(); r++) {
      List<Character> line = lines.get(r);
      List<Tile> row = new ArrayList<>();
      for (int c = 0; c < line.size(); c++) {
        row.add(new Tile(line.get(c), r, c));
      }
      map.add(row);
    }

    return "" + getEnergized(map, Triple.of(Direction.RIGHT, 0, 0));
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<List<Character>> lines = InputUtils.getCharacterArray(filename);
    List<List<Tile>> map = new ArrayList<>();

    for (int r = 0; r < lines.size(); r++) {
      List<Character> line = lines.get(r);
      List<Tile> row = new ArrayList<>();
      for (int c = 0; c < line.size(); c++) {
        row.add(new Tile(line.get(c), r, c));
      }
      map.add(row);
    }

    int max = 0;

    for (int c = 0; c < map.get(0).size(); c++) {
      // from top
      max = Math.max(max, getEnergized(map, Triple.of(Direction.DOWN, 0, c)));
      // from bottom
      max =
        Math.max(
          max,
          getEnergized(map, Triple.of(Direction.UP, map.size() - 1, c))
        );
    }

    for (int r = 0; r < map.size(); r++) {
      // from left
      max = Math.max(max, getEnergized(map, Triple.of(Direction.RIGHT, r, 0)));
      // from right
      max =
        Math.max(
          max,
          getEnergized(map, Triple.of(Direction.LEFT, r, map.get(0).size() - 1))
        );
    }

    return "" + max;
  }

  private int getEnergized(
    List<List<Tile>> map,
    Triple<Direction, Integer, Integer> start
  ) {
    Queue<Triple<Direction, Integer, Integer>> toGo = new LinkedList<>();
    toGo.add(start);

    Set<Pair<Integer, Integer>> visited = new HashSet<>();

    // prevent extra calls
    Map<String, Void> tracked = new HashMap<>();

    while (!toGo.isEmpty()) {
      Triple<Direction, Integer, Integer> next = toGo.poll();
      Tile.beamSafe(
        toGo,
        map,
        visited,
        tracked,
        next.getLeft(),
        next.getMiddle(),
        next.getRight()
      );
    }

    return visited.size();
  }

  @ToString
  public enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
  }

  @Data
  public static class Tile {

    char ch;

    int r;
    int c;

    public Tile(char ch, int r, int c) {
      this.ch = ch;
      this.r = r;
      this.c = c;
    }

    public static void beamSafe(
      Queue<Triple<Direction, Integer, Integer>> toVisit,
      List<List<Tile>> map,
      Set<Pair<Integer, Integer>> visited,
      Map<String, Void> tracked,
      Direction direction,
      int newR,
      int newC
    ) {
      if (newR < 0 || newR >= map.size()) {
        return;
      }
      if (newC < 0 || newC >= map.get(newR).size()) {
        return;
      }

      map.get(newR).get(newC).beam(toVisit, visited, tracked, direction);
    }

    public void beam(
      Queue<Triple<Direction, Integer, Integer>> toVisit,
      Set<Pair<Integer, Integer>> visited,
      Map<String, Void> tracked,
      Direction direction
    ) {
      visited.add(new Pair<>(r, c));

      String str = r + "," + c + "," + direction;
      if (tracked.containsKey(str)) {
        return;
      }
      tracked.put(str, null);

      switch (this.ch) {
        case '.' -> {
          switch (direction) {
            case LEFT -> toVisit.add(Triple.of(Direction.LEFT, r, c - 1));
            case RIGHT -> toVisit.add(Triple.of(Direction.RIGHT, r, c + 1));
            case UP -> toVisit.add(Triple.of(Direction.UP, r - 1, c));
            case DOWN -> toVisit.add(Triple.of(Direction.DOWN, r + 1, c));
          }
        }
        case '/' -> {
          switch (direction) {
            case LEFT -> toVisit.add(Triple.of(Direction.DOWN, r + 1, c)); // down
            case RIGHT -> toVisit.add(Triple.of(Direction.UP, r - 1, c)); // up
            case UP -> toVisit.add(Triple.of(Direction.RIGHT, r, c + 1)); // right
            case DOWN -> toVisit.add(Triple.of(Direction.LEFT, r, c - 1)); // left
          }
        }
        case '\\' -> {
          switch (direction) {
            case LEFT -> toVisit.add(Triple.of(Direction.UP, r - 1, c)); // up
            case RIGHT -> toVisit.add(Triple.of(Direction.DOWN, r + 1, c)); // down
            case UP -> toVisit.add(Triple.of(Direction.LEFT, r, c - 1)); // left
            case DOWN -> toVisit.add(Triple.of(Direction.RIGHT, r, c + 1)); // right
          }
        }
        case '-' -> {
          switch (direction) {
            case LEFT -> toVisit.add(Triple.of(Direction.LEFT, r, c - 1));
            case RIGHT -> toVisit.add(Triple.of(Direction.RIGHT, r, c + 1));
            case UP, DOWN -> {
              toVisit.add(Triple.of(Direction.LEFT, r, c - 1));
              toVisit.add(Triple.of(Direction.RIGHT, r, c + 1));
            }
          }
        }
        case '|' -> {
          switch (direction) {
            case UP -> toVisit.add(Triple.of(Direction.UP, r - 1, c));
            case DOWN -> toVisit.add(Triple.of(Direction.DOWN, r + 1, c));
            case LEFT, RIGHT -> {
              toVisit.add(Triple.of(Direction.UP, r - 1, c));
              toVisit.add(Triple.of(Direction.DOWN, r + 1, c));
            }
          }
        }
        default -> throw new RuntimeException("unknown char: " + ch);
      }
    }
  }
}
