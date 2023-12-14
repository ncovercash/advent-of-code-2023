package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day14 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<Character>> map = InputUtils.getCharacterArray(filename);

    while (tiltUp(map));

    int sum = 0;
    for (int r = 0; r < map.size(); r++) {
      for (int c = 0; c < map.get(r).size(); c++) {
        if (map.get(r).get(c) == 'O') {
          sum += map.size() - r;
        }
      }
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<List<Character>> map = InputUtils.getCharacterArray(filename);

    String lastEast = null;
    log.info("INIT\n" + getMap(map));

    long cyclesNeeded = 1000000000;
    List<String> cache = new ArrayList<>();

    while (cyclesNeeded > 0) {
      String mapStr = getMap(map);
      int index = cache.indexOf(mapStr);
      if (index >= 0) {
        log.info("FOUND REPEAT AT {}", index);
        int cycleLength = cache.size() - index;
        cyclesNeeded %= cycleLength;
        cache = new ArrayList<>();
        continue;
      }

      cache.add(mapStr);

      while (tiltUp(map));
      while (tiltLeft(map));
      while (tiltDown(map));
      while (tiltRight(map));

      cyclesNeeded--;
    }

    log.warn("ALL DONE ALL DONE ALL DONE ALL DONE\n" + getMap(map));

    int sum = 0;
    for (int r = 0; r < map.size(); r++) {
      for (int c = 0; c < map.get(r).size(); c++) {
        if (map.get(r).get(c) == 'O') {
          sum += map.size() - r;
        }
      }
    }

    return "" + sum;
  }

  String getMap(List<List<Character>> map) {
    return map
      .stream()
      .map(row ->
        row.stream().map(String::valueOf).collect(Collectors.joining())
      )
      .collect(Collectors.joining("\n"));
  }

  boolean tiltUp(List<List<Character>> map) {
    boolean moved = false;
    for (int r = 1; r < map.size(); r++) {
      for (int c = 0; c < map.get(r).size(); c++) {
        if (map.get(r).get(c) == 'O' && map.get(r - 1).get(c) == '.') {
          map.get(r).set(c, '.');
          map.get(r - 1).set(c, 'O');
          moved = true;
        }
      }
    }
    return moved;
  }

  boolean tiltDown(List<List<Character>> map) {
    boolean moved = false;
    for (int r = map.size() - 2; r >= 0; r--) {
      for (int c = 0; c < map.get(r).size(); c++) {
        if (map.get(r).get(c) == 'O' && map.get(r + 1).get(c) == '.') {
          map.get(r).set(c, '.');
          map.get(r + 1).set(c, 'O');
          moved = true;
        }
      }
    }
    return moved;
  }

  boolean tiltLeft(List<List<Character>> map) {
    boolean moved = false;
    for (int r = 0; r < map.size(); r++) {
      for (int c = 1; c < map.get(r).size(); c++) {
        if (map.get(r).get(c) == 'O' && map.get(r).get(c - 1) == '.') {
          map.get(r).set(c, '.');
          map.get(r).set(c - 1, 'O');
          moved = true;
        }
      }
    }
    return moved;
  }

  boolean tiltRight(List<List<Character>> map) {
    boolean moved = false;
    for (int r = 0; r < map.size(); r++) {
      for (int c = map.get(r).size() - 2; c >= 0; c--) {
        if (map.get(r).get(c) == 'O' && map.get(r).get(c + 1) == '.') {
          map.get(r).set(c, '.');
          map.get(r).set(c + 1, 'O');
          moved = true;
        }
      }
    }
    return moved;
  }
}
