package dev.ncovercash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class Day10Test {

  static List<Arguments> testCases() {
    return List.of(
      Arguments.of("10/example.txt", "4", "skip"),
      Arguments.of("10/example2.txt", "8", "skip"),
      Arguments.of("10/example3.txt", "skip", "4"),
      Arguments.of("10/example4.txt", "skip", "8"),
      Arguments.of("10/example5.txt", "skip", "10"),
      Arguments.of("10/input.txt", "7066", "401")
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testAnswer(String filename, String expectedPart1, String expectedPart2) {
    Day10 instance = new Day10();

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
