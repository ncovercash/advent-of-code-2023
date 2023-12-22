package dev.ncovercash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class Day19Test {

  static List<Arguments> testCases() {
    return List.of(
      Arguments.of("19/example.txt", "19114", "167409079868000"),
      Arguments.of("19/input.txt", "391132", "skip")
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testAnswer(String filename, String expectedPart1, String expectedPart2) {
    Day19 instance = new Day19();

    if (!"skip".equals(expectedPart1)) {
      int start = (int) System.currentTimeMillis();
      String actual = instance.completeChallengePartOne(filename);
      log.info(
        "Part 1: {} in {}ms",
        actual,
        (int) System.currentTimeMillis() - start
      );
      assertEquals(expectedPart1, actual);
    }

    if (!"skip".equals(expectedPart2)) {
      int start = (int) System.currentTimeMillis();
      String actual = instance.completeChallengePartTwo(filename);
      log.info(
        "Part 2: {} in {}ms",
        actual,
        (int) System.currentTimeMillis() - start
      );
      assertEquals(expectedPart2, actual);
    }
  }
}
