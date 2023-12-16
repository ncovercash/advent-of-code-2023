package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.util.Pair;

@Log4j2
public class Day15 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (String part : lines.get(0).split(",")) {
      sum += hash(part);
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    ArrayList<ArrayList<Pair<String, Integer>>> boxes = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      boxes.add(new ArrayList<>());
    }

    for (String part : lines.get(0).split(",")) {
      String label = part.split("=")[0].split("-")[0];
      int labelHash = hash(label);

      if (part.contains("-")) {
        boxes.get(labelHash).removeIf(p -> p.getKey().equals(label));
      } else {
        if (
          boxes.get(labelHash).stream().anyMatch(p -> p.getKey().equals(label))
        ) {
          for (int i = 0; i < boxes.get(labelHash).size(); i++) {
            if (boxes.get(labelHash).get(i).getKey().equals(label)) {
              boxes
                .get(labelHash)
                .set(
                  i,
                  new Pair<>(label, Integer.parseInt(part.split("=")[1]))
                );
            }
          }
        } else {
          boxes
            .get(labelHash)
            .add(new Pair<>(label, Integer.parseInt(part.split("=")[1])));
        }
      }
    }

    int sum = 0;
    for (int box = 0; box < 256; box++) {
      for (int lens = 0; lens < boxes.get(box).size(); lens++) {
        sum += (box + 1) * (lens + 1) * boxes.get(box).get(lens).getValue();
      }
    }

    return "" + sum;
  }

  public int hash(String str) {
    int result = 0;

    for (char ch : str.toCharArray()) {
      result += ch;
      result *= 17;
      result %= 256;
    }

    return result;
  }
}
