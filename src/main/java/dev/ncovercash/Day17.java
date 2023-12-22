package dev.ncovercash;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

@Log4j2
public class Day17 implements Solution {

  // @Override
  // public String completeChallengePartOne(String filename) {
  //   List<List<Character>> raw = InputUtils.getCharacterArray(filename);
  //   int[][] map = new int[raw.size()][raw.get(0).size()];
  //   for (int i = 0; i < raw.size(); i++) {
  //     for (int j = 0; j < raw.get(i).size(); j++) {
  //       map[i][j] = raw.get(i).get(j) - '0';
  //     }
  //   }

  //   Multimap<Pair<Integer, Integer>, Pair<Integer, String>> distance = HashMultimap.create();
  //   Set<Triple<Integer, Integer, String>> visited = new HashSet<>();

  //   distance.put(Pair.of(0, 0), Pair.of(0, "zzz"));

  //   try {
  //     while (true) {
  //       Map.Entry<Pair<Integer, Integer>, Pair<Integer, String>> node = distance
  //         .entries()
  //         .stream()
  //         .filter(e ->
  //           !visited.contains(
  //             Triple.of(
  //               e.getKey().getLeft(),
  //               e.getKey().getRight(),
  //               e.getValue().getRight()
  //             )
  //           )
  //         )
  //         .min((a, b) ->
  //           a.getValue().getLeft().compareTo(b.getValue().getLeft())
  //         )
  //         .orElseThrow();
  //       visited.add(
  //         Triple.of(
  //           node.getKey().getLeft(),
  //           node.getKey().getRight(),
  //           node.getValue().getRight()
  //         )
  //       );

  //       int distanceToNode = node.getValue().getLeft();
  //       String directionToNode = node.getValue().getRight();

  //       // can go up
  //       if (node.getKey().getLeft() > 0 && !"UUU".equals(directionToNode)) {
  //         considerPath(
  //           map,
  //           distance,
  //           distanceToNode,
  //           directionToNode,
  //           Pair.of(node.getKey().getLeft() - 1, node.getKey().getRight()),
  //           'U'
  //         );
  //       }
  //       // can go left
  //       if (node.getKey().getRight() > 0 && !"LLL".equals(directionToNode)) {
  //         considerPath(
  //           map,
  //           distance,
  //           distanceToNode,
  //           directionToNode,
  //           Pair.of(node.getKey().getLeft(), node.getKey().getRight() - 1),
  //           'L'
  //         );
  //       }
  //       // can go down
  //       if (
  //         node.getKey().getLeft() < map.length - 1 &&
  //         !"DDD".equals(directionToNode)
  //       ) {
  //         considerPath(
  //           map,
  //           distance,
  //           distanceToNode,
  //           directionToNode,
  //           Pair.of(node.getKey().getLeft() + 1, node.getKey().getRight()),
  //           'D'
  //         );
  //       }
  //       // can go right
  //       if (
  //         node.getKey().getRight() < map[0].length - 1 &&
  //         !"RRR".equals(directionToNode)
  //       ) {
  //         considerPath(
  //           map,
  //           distance,
  //           distanceToNode,
  //           directionToNode,
  //           Pair.of(node.getKey().getLeft(), node.getKey().getRight() + 1),
  //           'R'
  //         );
  //       }
  //     }
  //   } catch (NoSuchElementException ex) {
  //     return (
  //       "" +
  //       distance
  //         .entries()
  //         .stream()
  //         .filter(e -> e.getKey().getLeft() == map.length - 1)
  //         .filter(e -> e.getKey().getRight() == map[0].length - 1)
  //         .mapToInt(e -> e.getValue().getLeft())
  //         .min()
  //         .orElseThrow()
  //     );
  //   }
  // }

  // private void considerPath(
  //   int[][] map,
  //   Multimap<Pair<Integer, Integer>, Pair<Integer, String>> distance,
  //   int distanceToNode,
  //   String directionToNode,
  //   Pair<Integer, Integer> next,
  //   char direction
  // ) {
  //   Pair<Integer, String> curNext = distance
  //     .get(next)
  //     .stream()
  //     .filter(e -> e.getRight().equals(directionToNode.substring(1) + direction)
  //     )
  //     .findFirst()
  //     .orElse(Pair.of(Integer.MAX_VALUE, "zzz"));
  //   if (
  //     distanceToNode + map[next.getLeft()][next.getRight()] < curNext.getLeft()
  //   ) {
  //     distance.put(
  //       next,
  //       Pair.of(
  //         distanceToNode + map[next.getLeft()][next.getRight()],
  //         directionToNode.substring(1) + direction
  //       )
  //     );

  //     if (
  //       next.getLeft() == map.length - 1 && next.getRight() == map[0].length - 1
  //     ) {
  //       log.warn(
  //         "Found path: " +
  //         (distanceToNode + map[next.getLeft()][next.getRight()]) +
  //         " via " +
  //         direction
  //       );
  //     }
  //   }
  // }

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<Character>> raw = InputUtils.getCharacterArray(filename);
    int[][] map = new int[raw.size()][raw.get(0).size()];
    for (int i = 0; i < raw.size(); i++) {
      for (int j = 0; j < raw.get(i).size(); j++) {
        map[i][j] = raw.get(i).get(j) - '0';
      }
    }

    Multimap<Pair<Integer, Integer>, CachedValue> cache = HashMultimap.create();
    Map<Pair<Integer, Integer>, Void> visiting = new HashMap<>();

    return (
      "" + (getBestDistance(0, cache, visiting, map, 0, 0, null, 0) - map[0][0])
    );
  }

  private int getBestDistance(
    int l,
    Multimap<Pair<Integer, Integer>, CachedValue> cache,
    Map<Pair<Integer, Integer>, Void> visiting,
    int[][] map,
    int r,
    int c,
    Direction direction,
    int distance
  ) {
    log.info(
      "{}At {},{} with direction {} and distance {}",
      " ".repeat(l),
      r,
      c,
      direction,
      distance
    );
    if (cache.containsKey(Pair.of(r, c))) {
      int min = cache
        .get(Pair.of(r, c))
        .stream()
        // yikes?
        .filter(e -> e.direction() == direction && e.distance() <= distance)
        .mapToInt(e -> e.value())
        .min()
        .orElse(-1);

      if (min != -1) {
        return min;
      }
    }
    if (visiting.containsKey(Pair.of(r, c))) {
      return 999999 - map[r][c];
    }

    // ensure we don't get stuck in a loop
    visiting.put(Pair.of(r, c), null);

    if (r == map.length - 1 && c == map[0].length - 1) {
      return 0;
    }

    int min = Integer.MAX_VALUE;

    // can go up
    if (
      direction != Direction.DOWN &&
      r > 0 &&
      !(direction == Direction.UP && distance == 3)
    ) {
      min =
        Math.min(
          min,
          map[r - 1][c] +
          getBestDistance(
            l + 1,
            cache,
            visiting,
            map,
            r - 1,
            c,
            Direction.UP,
            direction == Direction.UP ? distance + 1 : 1
          )
        );
    }
    // can go left
    if (
      direction != Direction.RIGHT &&
      c > 0 &&
      !(direction == Direction.LEFT && distance == 3)
    ) {
      min =
        Math.min(
          min,
          map[r][c - 1] +
          getBestDistance(
            l + 1,
            cache,
            visiting,
            map,
            r,
            c - 1,
            Direction.LEFT,
            direction == Direction.LEFT ? distance + 1 : 1
          )
        );
    }
    // can go down
    if (
      direction != Direction.UP &&
      r < map.length - 1 &&
      !(direction == Direction.DOWN && distance == 3)
    ) {
      min =
        Math.min(
          min,
          map[r + 1][c] +
          getBestDistance(
            l + 1,
            cache,
            visiting,
            map,
            r + 1,
            c,
            Direction.DOWN,
            direction == Direction.DOWN ? distance + 1 : 1
          )
        );
    }
    // can go right
    if (
      direction != Direction.LEFT &&
      c < map[0].length - 1 &&
      !(direction == Direction.RIGHT && distance == 3)
    ) {
      min =
        Math.min(
          min,
          map[r][c + 1] +
          getBestDistance(
            l + 1,
            cache,
            visiting,
            map,
            r,
            c + 1,
            Direction.RIGHT,
            direction == Direction.RIGHT ? distance + 1 : 1
          )
        );
    }

    visiting.remove(Pair.of(r, c));
    cache.put(Pair.of(r, c), new CachedValue(direction, distance, min));

    return min;
  }

  @ToString
  public enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
  }

  public record CachedValue(Direction direction, int distance, int value) {}
}
