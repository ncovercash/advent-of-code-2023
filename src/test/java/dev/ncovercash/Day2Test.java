package dev.ncovercash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class Day2Test {

  static List<Arguments> testCases() {
    return List.of(
      Arguments.of("2/example.txt", "8", "2286"),
      Arguments.of("2/input.txt", "2317", "74804")
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testAnswer(String filename, String expectedPart1, String expectedPart2) {
    Day2 instance = new Day2();

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
