package dev.ncovercash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class Day20Test {

  static List<Arguments> testCases() {
    return List.of(
      Arguments.of("20/example1.txt", "32000000", "skip"),
      Arguments.of("20/example2.txt", "11687500", "skip"),
      Arguments.of("20/input.txt", "818649769", "246313604784977")
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testAnswer(String filename, String expectedPart1, String expectedPart2) {
    Day20 instance = new Day20();

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
