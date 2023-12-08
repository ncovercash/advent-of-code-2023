package dev.ncovercash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class Day8Test {

  static List<Arguments> testCases() {
    return List.of(
      Arguments.of("8/example1a.txt", "2", "skip"),
      Arguments.of("8/example1b.txt", "6", "skip"),
      Arguments.of("8/example2.txt", "skip", "6"),
      Arguments.of("8/input.txt", "17873", "15746133679061")
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testAnswer(String filename, String expectedPart1, String expectedPart2) {
    Day8 instance = new Day8();

    if (!"skip".equals(expectedPart1)) {
      String actual = instance.completeChallengePartOne(filename);
      log.info("Part 1: {}", actual);
      assertEquals(expectedPart1, actual);
    }

    if (!"skip".equals(expectedPart2)) {
      String actual = instance.completeChallengePartTwo(filename);
      log.info("Part 2: {}", actual);
      assertEquals(expectedPart2, actual);
    }
  }
}
