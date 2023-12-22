package dev.ncovercash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

@Log4j2
public class Day19 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<List<String>> groups = InputUtils.getLinesByClump(filename);

    Map<String, Workflow> workflows = new HashMap<>();

    for (String workflow : groups.get(0)) {
      String[] split = workflow.split("\\{");

      workflows.put(
        split[0],
        new Workflow(split[0], split[1].substring(0, split[1].length() - 1))
      );
    }

    List<Part> parts = new ArrayList<>();

    for (String part : groups.get(1)) {
      String[] attrs = part.substring(1, part.length() - 1).split(",");
      parts.add(
        new Part(
          Integer.parseInt(attrs[0].split("=")[1]),
          Integer.parseInt(attrs[1].split("=")[1]),
          Integer.parseInt(attrs[2].split("=")[1]),
          Integer.parseInt(attrs[3].split("=")[1])
        )
      );
    }

    int sum = 0;
    for (Part part : parts) {
      if (workflows.get("in").apply(part, workflows)) {
        sum += part.x;
        sum += part.m;
        sum += part.a;
        sum += part.s;
      }
    }

    return "" + sum;
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<List<String>> groups = InputUtils.getLinesByClump(filename);

    Map<String, Workflow> workflows = new HashMap<>();

    for (String workflow : groups.get(0)) {
      String[] split = workflow.split("\\{");

      workflows.put(
        split[0],
        new Workflow(split[0], split[1].substring(0, split[1].length() - 1))
      );
    }

    PartRange partRange = new PartRange(
      Arrays.asList(Range.of(0, 4000)),
      Arrays.asList(Range.of(0, 4000)),
      Arrays.asList(Range.of(0, 4000)),
      Arrays.asList(Range.of(0, 4000))
    );

    List<PartRange> acceptable = workflows
      .get("in")
      .apply(0, partRange, workflows);
    long sum = 0;

    for (PartRange range : acceptable) {
      sum +=
        range.x.stream().mapToLong(Range::size).sum() *
        range.m.stream().mapToLong(Range::size).sum() *
        range.a.stream().mapToLong(Range::size).sum() *
        range.s.stream().mapToLong(Range::size).sum();
    }

    return "" + sum;
  }

  @Data
  private static class Workflow {

    private String name;
    private List<Rule> rules = new ArrayList<>();

    public Workflow(String name, String ruleset) {
      this.name = name;
      String[] ruleStrings = ruleset.split(",");
      for (String rule : ruleStrings) {
        String[] condDest = rule.split(":");

        if (condDest.length == 1) {
          rules.add(new Rule('z', 'z', 0, condDest[0]));
        } else {
          char attr = condDest[0].charAt(0);
          char cond = condDest[0].charAt(1);
          int threshold = Integer.parseInt(condDest[0].substring(2));

          rules.add(new Rule(attr, cond, threshold, condDest[1]));
        }
      }
    }

    boolean apply(Part part, Map<String, Workflow> workflows) {
      Workflow workflow = this;

      while (true) {
        for (Rule rule : workflow.rules) {
          if (Boolean.TRUE.equals(rule.apply(part))) {
            if (rule.destination.equals("R")) {
              return false;
            } else if (rule.destination.equals("A")) {
              return true;
            } else {
              workflow = workflows.get(rule.destination);
            }
            break;
          }
        }
      }
    }

    List<PartRange> apply(
      int l,
      PartRange range,
      Map<String, Workflow> workflows
    ) {
      log.info("{}{} with {}", " ".repeat(l), name, range);
      List<PartRange> accepted = new ArrayList<>();

      for (Rule rule : rules) {
        PartRange matching = range.getMatching(rule);
        PartRange nonMatching = range.getNonMatching(rule);
        range = nonMatching;

        // log.info(
        //   "{}rule {}{}{} matches {}",
        //   " ".repeat(l + 1),
        //   rule.attr,
        //   rule.cond,
        //   rule.threshold,
        //   matching
        // );

        if (rule.destination.equals("R")) {
          continue;
        } else if (rule.destination.equals("A")) {
          accepted.add(matching);
        } else {
          accepted.addAll(
            workflows.get(rule.destination).apply(l + 2, matching, workflows)
          );
        }
      }

      log.info("{}{} accepted: {}", " ".repeat(l), name, accepted);

      return accepted;
    }
  }

  @Data
  @AllArgsConstructor
  private class Part {

    public int x;
    public int m;
    public int a;
    public int s;

    int get(char ch) {
      return switch (ch) {
        case 'x' -> x;
        case 'm' -> m;
        case 'a' -> a;
        case 's' -> s;
        default -> throw new IllegalArgumentException("Invalid part: " + ch);
      };
    }
  }

  record PartRange(List<Range> x, List<Range> m, List<Range> a, List<Range> s) {
    public static List<Range> truncate(List<Range> ranges, int min, int max) {
      return ranges
        .stream()
        .map(r -> r.truncatedTo(min, max))
        .filter(r -> r != null)
        .toList();
    }

    public PartRange truncate(char attr, int min, int max) {
      return switch (attr) {
        case 'x' -> new PartRange(
          new ArrayList<>(truncate(x, min, max)),
          m,
          a,
          s
        );
        case 'm' -> new PartRange(
          x,
          new ArrayList<>(truncate(m, min, max)),
          a,
          s
        );
        case 'a' -> new PartRange(
          x,
          m,
          new ArrayList<>(truncate(a, min, max)),
          s
        );
        case 's' -> new PartRange(
          x,
          m,
          a,
          new ArrayList<>(truncate(s, min, max))
        );
        default -> throw new IllegalArgumentException("Invalid part: " + attr);
      };
    }

    public PartRange getMatching(Rule rule) {
      if (rule.attr() == 'z') {
        return this;
      }
      if (rule.cond() == '<') {
        return truncate(rule.attr(), 0, rule.threshold() - 1);
      } else {
        return truncate(rule.attr(), rule.threshold() + 1, 4000);
      }
    }

    public PartRange getNonMatching(Rule rule) {
      if (rule.attr() == 'z') {
        // nothing
        return new PartRange(
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>()
        );
      }
      if (rule.cond() == '<') {
        return truncate(rule.attr(), rule.threshold(), 4000);
      } else {
        return truncate(rule.attr(), 0, rule.threshold());
      }
    }
  }

  record Rule(char attr, char cond, int threshold, String destination) {
    public boolean apply(Part part) {
      return switch (cond) {
        case '<' -> part.get(attr) < threshold;
        case '>' -> part.get(attr) > threshold;
        default -> true;
      };
    }
  }
}
