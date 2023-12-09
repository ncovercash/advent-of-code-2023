package dev.ncovercash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.ArithmeticUtils;

@Log4j2
public class Day8 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<String>> puzzle = InputUtils.getLinesByClump(filename);

    List<Character> instructions = puzzle
      .get(0)
      .get(0)
      .chars()
      .mapToObj(c -> (char) c)
      .toList();

    Map<String, Pair<String, String>> locations = new HashMap<>();

    for (String node : puzzle.get(1)) {
      String[] parts = node.split(" = ");
      String src = parts[0];
      parts[1] = parts[1].replace("(", "").replace(")", "");
      String[] dest = parts[1].split(", ");

      locations.put(src, Pair.of(dest[0], dest[1]));
    }

    String curLocation = "AAA";
    int numSteps = 0;
    for (int i = 0; true; i++) {
      char direction = instructions.get(i % instructions.size());

      if (direction == 'L') {
        curLocation = locations.get(curLocation).getLeft();
      } else if (direction == 'R') {
        curLocation = locations.get(curLocation).getRight();
      }

      numSteps++;

      if (curLocation.equals("ZZZ")) {
        break;
      }
    }

    return "" + numSteps;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<List<String>> puzzle = InputUtils.getLinesByClump(filename);

    List<Character> instructions = puzzle
      .get(0)
      .get(0)
      .chars()
      .mapToObj(c -> (char) c)
      .toList();

    Map<String, Pair<String, String>> locations = new HashMap<>();

    for (String node : puzzle.get(1)) {
      String[] parts = node.split(" = ");
      String src = parts[0];
      parts[1] = parts[1].replace("(", "").replace(")", "");
      String[] dest = parts[1].split(", ");

      locations.put(src, Pair.of(dest[0], dest[1]));
    }

    List<String> curLocations = locations
      .keySet()
      .stream()
      .filter(key -> key.endsWith("A"))
      .collect(Collectors.toList());

    List<Integer> steps = curLocations
      .stream()
      .map(l -> {
        String curLocation = l;

        int numSteps = 0;
        for (int i = 0; true; i++) {
          char direction = instructions.get(i % instructions.size());

          if (direction == 'L') {
            curLocation = locations.get(curLocation).getLeft();
          } else if (direction == 'R') {
            curLocation = locations.get(curLocation).getRight();
          }

          numSteps++;

          if (curLocation.endsWith("Z")) {
            break;
          }
        }

        return numSteps;
      })
      .toList();

    long lcm = 1;
    for (int i = 0; i < steps.size(); i++) {
      lcm = ArithmeticUtils.lcm(lcm, steps.get(i));
    }

    return "" + lcm;
  }
}
