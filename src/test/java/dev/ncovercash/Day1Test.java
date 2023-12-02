package dev.ncovercash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Log4j2
class Day1Test {

  static List<Arguments> testCases() {
    return List.of(
      Arguments.of("1/example.txt", "1412", ""),
      Arguments.of("1/input.txt", "", "")
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testAnswer(String filename, String expectedPart1, String expectedPart2) {
    Day1 instance = new Day1();

    String actual = instance.completeChallengePartOne(filename);
    log.info("Day 1 Part 1: {}", actual);
    assertEquals(expectedPart1, actual);

    actual = instance.completeChallengePartTwo(filename);
    log.info("Day 1 Part 2: {}", actual);
    assertEquals(expectedPart2, actual);
  }
}
