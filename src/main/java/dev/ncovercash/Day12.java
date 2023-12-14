package dev.ncovercash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day12 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    long sum = 0;

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

      long n = getNumOptions(
        chunks.stream().collect(Collectors.joining(".")),
        0,
        needed,
        needed.stream().mapToInt(i -> i).sum() + needed.size() - 1,
        new HashMap<>()
      );
      log.info("{} with {} => {}", game, needed, n);
      sum += n;
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    AtomicLong sum = new AtomicLong(0);

    ExecutorService executor = Executors.newFixedThreadPool(10);

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

      executor.submit(() -> {
        long n = getNumOptions(
          chunks.stream().collect(Collectors.joining(".")),
          0,
          needed,
          needed.stream().mapToInt(i -> i).sum() + needed.size() - 1,
          new HashMap<>()
        );
        log.info("{} with {} => {}", game, needed, n);
        sum.addAndGet(n);
      });
    }

    try {
      executor.shutdown();
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return "" + sum;
  }

  private long getNumOptions(
    final String game,
    final int st,
    final List<Integer> needed,
    final int minLength,
    final Map<String, Long> memo
  ) {
    String memoKey = st + "," + needed.size();
    if (memo.containsKey(memoKey)) {
      return memo.get(memoKey);
    }

    if (game.length() - st < minLength) {
      return 0;
    }
    // // have too many required chunks
    // // may be premature?
    // if (
    //   needed.size() < chunks.stream().filter(str -> str.contains("#")).count()
    // ) {
    //   return 0;
    // }
    // all done!
    if (needed.isEmpty()) {
      // we can't have leftover #
      long val = game.indexOf("#", st) >= 0 ? 0L : 1L;
      memo.put(memoKey, val);
      return val;
    }
    // some still needed, but no chunks left
    if (st >= game.length()) {
      return 0;
    }

    if (game.charAt(st) == '.') {
      long val = getNumOptions(game, st + 1, needed, minLength, memo);
      memo.put(memoKey, val);
      return val;
    }

    int neededSize = needed.get(0);
    int indexOfChunkEnd = game.indexOf(".", st);
    int indexOfNextChunkStart = indexOfChunkEnd + 1;
    if (indexOfChunkEnd == -1) {
      indexOfChunkEnd = game.length();
      indexOfNextChunkStart = game.length();
    }
    String chunk = game.substring(st, indexOfChunkEnd);

    // first chunk cannot satisfy
    if (chunk.length() < neededSize) {
      if (chunk.contains("#")) {
        // we can't skip this.
        return 0;
      }
      long val = getNumOptions(
        game,
        indexOfNextChunkStart,
        needed,
        minLength,
        memo
      );
      memo.put(memoKey, val);
      return val;
    }

    // first chunk can go full ###
    if (chunk.length() == neededSize) {
      // return doing and not doing this
      long possibleSkipping = 0;
      if (!chunk.contains("#")) {
        possibleSkipping =
          getNumOptions(game, indexOfNextChunkStart, needed, minLength, memo);
      }

      long val =
        (
          getNumOptions(
            game,
            indexOfNextChunkStart,
            needed.subList(1, needed.size()),
            Math.max(minLength - neededSize - 1, 0),
            memo
          ) +
          possibleSkipping
        );
      memo.put(memoKey, val);
      return val;
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

    int skipThisChunk;
    if (numHashAtStart > 0) {
      // we can't skip #, so we fail it
      skipThisChunk = Integer.MAX_VALUE;
    } else {
      // remove a ?, try again
      skipThisChunk = st + 1;
    }

    // we don't fit
    if (chunk.charAt(neededSize) == '#') {
      long val = getNumOptions(game, skipThisChunk, needed, minLength, memo);
      memo.put(memoKey, val);
      return val;
    }

    // log.info("WE CAN FIT {} in {}", neededSize, chunk);
    long val =
      (
        getNumOptions(
          game,
          st + neededSize + 1,
          needed.subList(1, needed.size()),
          Math.max(minLength - neededSize - 1, 0),
          memo
        ) +
        getNumOptions(game, skipThisChunk, needed, minLength, memo)
      );
    memo.put(memoKey, val);
    return val;
  }
}
