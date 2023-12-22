package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Range implements Comparable<Range> {

  int min;
  int max;

  @Override
  public int compareTo(Range y) {
    return this.min - y.min;
  }

  public static List<Range> compact(List<Range> ranges) {
    ranges = ranges.stream().sorted().toList();

    int curMin = ranges.get(0).min;
    int lastMax = ranges.get(0).max;

    List<Range> collapsed = new ArrayList<>();

    for (Range r : ranges) {
      if (r.isEmpty()) {
        continue;
      }

      if (r.min > lastMax + 1) {
        collapsed.add(new Range(curMin, lastMax));
        curMin = r.min;
      }

      lastMax = Math.max(lastMax, r.max);
    }

    collapsed.add(new Range(curMin, lastMax));

    return collapsed;
  }

  public int size() {
    return max - min + 1;
  }

  /** MODIFIES IN PLACE */
  public Range truncate(int newMin, int newMax) {
    // completely OOB
    if (this.max < newMin || this.min > newMax) {
      this.min = -1;
      this.max = -1;
    } else {
      this.min = Math.max(this.min, newMin);
      this.max = Math.min(this.max, newMax);
    }

    // chaining go brrr
    return this;
  }

  /** Clones into a new range */
  @CheckForNull
  public Range truncatedTo(int newMin, int newMax) {
    if (newMin > newMax) {
      return null;
    }
    return this.clone().truncate(newMin, newMax);
  }

  public boolean isEmpty() {
    return this.min == -1 && this.max == -1;
  }

  @Override
  public String toString() {
    return String.format("[%d,%d]", min, max);
  }

  public Range clone() {
    return new Range(this.min, this.max);
  }

  public static Range of(int min, int max) {
    return new Range(min, max);
  }
}
