package dev.ncovercash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

@Log4j2
public class Day12 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (String line : lines) {
      String game = line.split(" ")[0];
      List<Integer> needed = Arrays
        .stream(line.split(" ")[1].split(","))
        .map(Integer::parseInt)
        .toList();

      List<String> chunks = Arrays
        .stream(game.split("\\."))
        .filter(str -> !str.isBlank())
        .toList();

      int n = getNumOptions(
        chunks,
        needed,
        needed.stream().mapToInt(i -> i).sum()
      );
      log.info("{} with {} => {}", game, needed, n);
      sum += n;
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    int sum = 0;

    for (String line : lines) {
      String game =
        line.split(" ")[0] +
        "?" +
        line.split(" ")[0] +
        "?" +
        line.split(" ")[0] +
        "?" +
        line.split(" ")[0] +
        "?" +
        line.split(" ")[0];
      List<Integer> needed = Arrays
        .stream(
          (
            line.split(" ")[1] +
            "," +
            line.split(" ")[1] +
            "," +
            line.split(" ")[1] +
            "," +
            line.split(" ")[1] +
            "," +
            line.split(" ")[1]
          ).split(",")
        )
        .map(Integer::parseInt)
        .toList();

      List<String> chunks = Arrays
        .stream(game.split("\\."))
        .filter(str -> !str.isBlank())
        .toList();

      int n = getNumOptions(
        chunks,
        needed,
        needed.stream().mapToInt(i -> i).sum()
      );
      log.info("{} with {} => {}", game, needed, n);
      sum += n;
    }

    return "" + sum;
  }

  private int getNumOptions(
    List<String> chunks,
    List<Integer> needed,
    int minLength
  ) {
    if (chunks.stream().mapToInt(String::length).sum() < minLength) {
      return 0;
    }
    // have too many required chunks
    // may be premature?
    if (
      needed.size() < chunks.stream().filter(str -> str.contains("#")).count()
    ) {
      return 0;
    }
    // all done!
    if (needed.isEmpty()) {
      return 1;
    }
    // some still needed, but no chunks left
    if (chunks.isEmpty()) {
      return 0;
    }

    int neededSize = needed.get(0);
    String chunk = chunks.get(0);

    // first chunk cannot satisfy
    if (chunk.length() < neededSize) {
      if (chunk.contains("#")) {
        // we can't skip this.
        return 0;
      }
      return getNumOptions(chunks.subList(1, chunks.size()), needed, minLength);
    }

    // first chunk can go full ###
    if (chunk.length() == neededSize) {
      // return doing and not doing this
      int possibleSkipping = 0;
      if (!chunk.contains("#")) {
        possibleSkipping =
          getNumOptions(chunks.subList(1, chunks.size()), needed, minLength);
      }

      return (
        getNumOptions(
          chunks.subList(1, chunks.size()),
          needed.subList(1, needed.size()),
          minLength - neededSize
        ) +
        possibleSkipping
      );
    }

    // if we have # at the start and are skipping forward, this cannot work.
    int numHashAtStart = 0;
    for (int i = 0; i < chunk.length(); i++) {
      if (chunk.charAt(i) == '#') {
        numHashAtStart++;
      } else {
        break;
      }
    }

    // too big
    if (numHashAtStart > neededSize) {
      return 0;
    }

    List<String> skipThisChunk;
    if (numHashAtStart > 0) {
      // we can't skip #, so we fail it
      skipThisChunk = new ArrayList<>();
    } else {
      // remove a ?, try again
      skipThisChunk = new ArrayList<>(chunks);
      skipThisChunk.set(0, chunk.substring(1));
    }

    // we don't fit
    if (chunk.charAt(neededSize) == '#') {
      return getNumOptions(skipThisChunk, needed, minLength);
    }

    List<String> newChunks = new ArrayList<>(chunks);
    // we fit!
    // take an extra slot for the gap between chunks
    newChunks.set(0, chunk.substring(neededSize + 1));
    // log.info("WE CAN FIT {} in {}", neededSize, chunk);
    return (
      getNumOptions(
        newChunks,
        needed.subList(1, needed.size()),
        minLength - neededSize
      ) +
      getNumOptions(skipThisChunk, needed, minLength)
    );
  }
}
