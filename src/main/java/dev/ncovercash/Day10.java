package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

@Log4j2
public class Day10 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<List<Pipe>> pipes = lines
      .stream()
      .map(line -> line.chars().mapToObj(c -> (char) c).map(Pipe::new).toList())
      .toList();

    String str = "";
    for (int i = 0; i < pipes.size(); i++) {
      for (int j = 0; j < pipes.get(i).size(); j++) {
        str += pipes.get(i).get(j).toString();
      }
      str += "\n";
    }
    log.info(str);

    Pair<Integer, Integer> start = Pair.of(0, 0);

    for (int i = 0; i < pipes.size(); i++) {
      for (int j = 0; j < pipes.get(i).size(); j++) {
        if (pipes.get(i).get(j).isMystery()) {
          start = Pair.of(i, j);
        }
      }
    }

    List<Pair<Integer, Integer>> path = new ArrayList<>();
    Pair<Integer, Integer> current = start;

    do {
      path.add(current);

      List<Pair<Integer, Integer>> directions = getAvailableDirections(
        pipes,
        current
      );

      if (directions.get(0).equals(start) && path.size() > 2) {
        current = directions.get(0);
      } else if (directions.get(1).equals(start) && path.size() > 2) {
        current = directions.get(1);
      } else if (path.contains(directions.get(0))) {
        current = directions.get(1);
      } else {
        current = directions.get(0);
      }
    } while (!current.equals(start));

    return "" + path.size() / 2;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<List<Pipe>> pipes = lines
      .stream()
      .map(line -> line.chars().mapToObj(c -> (char) c).map(Pipe::new).toList())
      .toList();

    String str = "";
    for (int i = 0; i < pipes.size(); i++) {
      for (int j = 0; j < pipes.get(i).size(); j++) {
        str += pipes.get(i).get(j).toString();
      }
      str += "\n";
    }
    log.info(str);

    Pair<Integer, Integer> start = Pair.of(0, 0);

    for (int i = 0; i < pipes.size(); i++) {
      for (int j = 0; j < pipes.get(i).size(); j++) {
        if (pipes.get(i).get(j).isMystery()) {
          start = Pair.of(i, j);
        }
      }
    }

    List<Pair<Integer, Integer>> path = new ArrayList<>();
    Pair<Integer, Integer> current = start;

    do {
      path.add(current);

      List<Pair<Integer, Integer>> directions = getAvailableDirections(
        pipes,
        current
      );

      if (directions.get(0).equals(start) && path.size() > 2) {
        current = directions.get(0);
      } else if (directions.get(1).equals(start) && path.size() > 2) {
        current = directions.get(1);
      } else if (path.contains(directions.get(0))) {
        current = directions.get(1);
      } else {
        current = directions.get(0);
      }
    } while (!current.equals(start));

    List<Pair<Integer, Integer>> startDirections = getAvailableDirections(
      pipes,
      start
    );
    pipes
      .get(start.getLeft())
      .get(start.getRight())
      .fillInFrom(startDirections, start);

    int numInside = 0;
    for (int i = 0; i < pipes.size(); i++) {
      for (int j = 0; j < pipes.get(i).size(); j++) {
        if (!path.contains(Pair.of(i, j))) {
          int numCrossed = 0;
          boolean crossedUpFromLeft = true;
          // travel to top
          for (int k = i - 1; k >= 0; k--) {
            if (path.contains(Pair.of(k, j))) {
              if (
                // straight horizontal -
                !pipes.get(k).get(j).isUp() && !pipes.get(k).get(j).isDown()
              ) {
                numCrossed++;
              } else if (
                // straight vertical |
                pipes.get(k).get(j).isUp() && pipes.get(k).get(j).isDown()
              ) {
                // do nothing
              } else if (pipes.get(k).get(j).isUp()) {
                crossedUpFromLeft = pipes.get(k).get(j).isLeft();
              } else if (pipes.get(k).get(j).isDown()) {
                if (crossedUpFromLeft && pipes.get(k).get(j).isRight()) {
                  numCrossed++;
                } else if (!crossedUpFromLeft && pipes.get(k).get(j).isLeft()) {
                  numCrossed++;
                }
              }
            }
          }

          if (numCrossed % 2 == 1) {
            numInside++;
            log.info("({},{})", i, j);
          }
        }
      }
    }

    return "" + numInside;
  }

  public static List<Pair<Integer, Integer>> getAvailableDirections(
    List<List<Pipe>> map,
    Pair<Integer, Integer> point
  ) {
    List<Pair<Integer, Integer>> directions = new ArrayList<>();

    Pipe thisPipe = map.get(point.getLeft()).get(point.getRight());

    if (
      thisPipe.isUp() &&
      point.getLeft() > 0 &&
      map.get(point.getLeft() - 1).get(point.getRight()).isDown()
    ) {
      directions.add(Pair.of(point.getLeft() - 1, point.getRight()));
    }
    if (
      thisPipe.isDown() &&
      point.getLeft() < map.size() - 1 &&
      map.get(point.getLeft() + 1).get(point.getRight()).isUp()
    ) {
      directions.add(Pair.of(point.getLeft() + 1, point.getRight()));
    }
    if (
      thisPipe.isLeft() &&
      point.getRight() > 0 &&
      map.get(point.getLeft()).get(point.getRight() - 1).isRight()
    ) {
      directions.add(Pair.of(point.getLeft(), point.getRight() - 1));
    }
    if (
      thisPipe.isRight() &&
      point.getRight() < map.get(point.getLeft()).size() - 1 &&
      map.get(point.getLeft()).get(point.getRight() + 1).isLeft()
    ) {
      directions.add(Pair.of(point.getLeft(), point.getRight() + 1));
    }

    return directions;
  }

  @Getter
  public static class Pipe {

    private char ch;

    private boolean empty = false;

    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private boolean mystery = false;

    public Pipe(char ch) {
      this.ch = ch;

      switch (ch) {
        case '.':
          empty = true;
          return;
        case '|':
          up = true;
          down = true;
          return;
        case '-':
          left = true;
          right = true;
          return;
        case 'L':
          up = true;
          right = true;
          return;
        case 'J':
          up = true;
          left = true;
          return;
        case '7':
          down = true;
          left = true;
          return;
        case 'F':
          down = true;
          right = true;
          return;
        case 'S':
          up = true;
          down = true;
          left = true;
          right = true;
          mystery = true;
          return;
        default:
          throw new IllegalArgumentException("Unknown pipe: " + ch);
      }
    }

    public void fillInFrom(
      List<Pair<Integer, Integer>> directions,
      Pair<Integer, Integer> point
    ) {
      up = directions.contains(Pair.of(point.getLeft() - 1, point.getRight()));
      down =
        directions.contains(Pair.of(point.getLeft() + 1, point.getRight()));
      left =
        directions.contains(Pair.of(point.getLeft(), point.getRight() - 1));
      right =
        directions.contains(Pair.of(point.getLeft(), point.getRight() + 1));

      mystery = false;
    }

    public String toString() {
      switch (ch) {
        case '.':
          return " ";
        case '|':
          return "│";
        case '-':
          return "─";
        case 'L':
          return "└";
        case 'J':
          return "┘";
        case '7':
          return "┐";
        case 'F':
          return "┌";
        case 'S':
          return "O";
        default:
          throw new IllegalArgumentException("Unknown pipe: " + ch);
      }
    }
  }
}
