package dev.ncovercash;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

@Log4j2
public class Day7 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<Pair<Hand, Integer>> hands = lines
      .stream()
      .map(line ->
        Pair.of(
          new Hand(
            line
              .split(" ")[0].chars()
              .mapToObj(c -> (char) c)
              .collect(Collectors.toList())
          ),
          Integer.parseInt(line.split(" ")[1])
        )
      )
      .sorted()
      .toList();

    int totalScore = 0;

    for (int i = 0; i < hands.size(); i++) {
      totalScore += hands.get(i).getRight() * (i + 1);
    }

    return "" + totalScore;
  }

  @Data
  public static class Hand implements Comparable<Hand> {

    private static final List<Character> VALUES = Arrays.asList(
      'A',
      'K',
      'Q',
      'J',
      'T',
      '9',
      '8',
      '7',
      '6',
      '5',
      '4',
      '3',
      '2'
    );

    private List<Character> cards;
    // 7: five of a kind
    // 6: four of a kind
    // 5: full house
    // 4: three of a kind
    // 3: two pair
    // 2: one pair
    // 1: none of the above
    private int score;

    public Hand(List<Character> cards) {
      this.cards = cards;

      Map<Character, Long> counts = cards
        .stream()
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

      if (counts.size() == 1) {
        this.score = 7;
      } else if (counts.size() == 2) {
        if (counts.values().contains(4L)) {
          this.score = 6; // 4/1
        } else {
          this.score = 5; // 3/2
        }
      } else if (counts.size() == 3) {
        if (counts.values().contains(3L)) {
          this.score = 4; // 3/1/1
        } else {
          this.score = 3; // 2/2/1
        }
      } else if (counts.size() == 4) {
        this.score = 2; // 2/1/1/1
      } else {
        this.score = 1; // 1/1/1/1/1
      }
    }

    @Override
    public int compareTo(Hand o) {
      if (this.score != o.score) {
        return Integer.compare(this.score, o.score);
      }

      for (int i = 0; i < this.cards.size(); i++) {
        if (this.cards.get(i) != o.cards.get(i)) {
          return -Integer.compare(
            VALUES.indexOf(this.cards.get(i)),
            VALUES.indexOf(o.cards.get(i))
          );
        }
      }

      return 0;
    }
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    List<Pair<Hand2, Integer>> hands = lines
      .stream()
      .map(line ->
        Pair.of(
          new Hand2(
            line
              .split(" ")[0].chars()
              .mapToObj(c -> (char) c)
              .collect(Collectors.toList())
          ),
          Integer.parseInt(line.split(" ")[1])
        )
      )
      .sorted()
      .toList();

    int totalScore = 0;

    for (int i = 0; i < hands.size(); i++) {
      totalScore += hands.get(i).getRight() * (i + 1);
    }

    return "" + totalScore;
  }

  @Data
  public static class Hand2 implements Comparable<Hand2> {

    private static final List<Character> VALUES = Arrays.asList(
      'A',
      'K',
      'Q',
      'T',
      '9',
      '8',
      '7',
      '6',
      '5',
      '4',
      '3',
      '2',
      'J'
    );

    private List<Character> cards;
    // 7: five of a kind
    // 6: four of a kind
    // 5: full house
    // 4: three of a kind
    // 3: two pair
    // 2: one pair
    // 1: none of the above
    private int score;

    public Hand2(List<Character> cards) {
      this.cards = cards;

      int numJokers = (int) cards.stream().filter(c -> c == 'J').count();

      Map<Character, Long> counts = cards
        .stream()
        .filter(c -> c != 'J')
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

      if (counts.size() <= 1) {
        this.score = 7;
      } else if (counts.size() == 2) {
        if (counts.values().contains(4L - numJokers)) {
          this.score = 6; // 4/1
        } else {
          this.score = 5; // 3/2
        }
      } else if (counts.size() == 3) {
        if (counts.values().contains(3L - numJokers)) {
          this.score = 4; // 3/1/1
        } else {
          this.score = 3; // 2/2/1
        }
      } else if (counts.size() == 4) {
        this.score = 2; // 2/1/1/1
      } else {
        this.score = 1; // 1/1/1/1/1
      }
    }

    @Override
    public int compareTo(Hand2 o) {
      if (this.score != o.score) {
        return Integer.compare(this.score, o.score);
      }

      for (int i = 0; i < this.cards.size(); i++) {
        if (this.cards.get(i) != o.cards.get(i)) {
          return -Integer.compare(
            VALUES.indexOf(this.cards.get(i)),
            VALUES.indexOf(o.cards.get(i))
          );
        }
      }

      return 0;
    }
  }
}
