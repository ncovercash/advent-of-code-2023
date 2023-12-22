package dev.ncovercash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.MathUtils;

@Log4j2
public class Day20 implements Solution {

  @Override
  public String completeChallengePartOne(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    Map<String, Module> modules = new HashMap<>();
    for (String line : lines) {
      String name = line.split(" -> ")[0];
      List<String> destinations = Arrays.asList(
        line.split(" -> ")[1].split(", ")
      );

      if (name.startsWith("broadcaster")) {
        modules.put(name, new Broadcast(name, destinations));
      } else if (name.startsWith("%")) {
        modules.put(
          name.substring(1),
          new FlipFlop(name.substring(1), destinations)
        );
      } else if (name.startsWith("&")) {
        modules.put(
          name.substring(1),
          new Conjunction(name.substring(1), destinations)
        );
      }
    }

    for (Module module : modules.values()) {
      module.registerInputs(modules);
    }

    Queue<Pulse> pulses = new LinkedList<>();

    int low = 0;
    int high = 0;

    for (int i = 0; i < 1000; i++) {
      pulses.add(new Pulse("button", "broadcaster", false));

      while (!pulses.isEmpty()) {
        Pulse pulse = pulses.poll();

        if (pulse.value()) {
          high++;
        } else {
          low++;
        }
        List<Pulse> newPulses = modules
          .computeIfAbsent(pulse.to(), k -> new NoOp())
          .process(pulse.from(), pulse.value());
        pulses.addAll(newPulses);
      }
    }

    return "" + (low * high);
  }

  @Override
  public String completeChallengePartTwo(String filename) {
    List<String> lines = InputUtils.getLines(filename);

    Map<String, Module> modules = new HashMap<>();
    for (String line : lines) {
      String name = line.split(" -> ")[0];
      List<String> destinations = Arrays.asList(
        line.split(" -> ")[1].split(", ")
      );

      if (name.startsWith("broadcaster")) {
        modules.put(name, new Broadcast(name, destinations));
      } else if (name.startsWith("%")) {
        modules.put(
          name.substring(1),
          new FlipFlop(name.substring(1), destinations)
        );
      } else if (name.startsWith("&")) {
        modules.put(
          name.substring(1),
          new Conjunction(name.substring(1), destinations)
        );
      }
    }

    for (Module module : modules.values()) {
      module.registerInputs(modules);
    }

    Queue<Pulse> pulses = new LinkedList<>();

    // we need all inputs to TJ to be true
    Map<String, Integer> tjInputs = new HashMap<>();
    ((Conjunction) modules.get("tj")).getLastValues()
      .keySet()
      .forEach(k -> tjInputs.put(k, -1));

    for (int i = 0; true; i++) {
      pulses.add(new Pulse("button", "broadcaster", false));

      while (!pulses.isEmpty()) {
        Pulse pulse = pulses.poll();

        if ("tj".equals(pulse.to()) && pulse.value()) {
          if (tjInputs.get(pulse.from()) == -1) {
            tjInputs.put(pulse.from(), i + 1);
            log.info("{} at {}", pulse.from(), (i + 1));
          }

          if (tjInputs.values().stream().allMatch(v -> v != -1)) {
            return (
              "" +
              tjInputs
                .values()
                .stream()
                .mapToLong(v -> v)
                .reduce(1, ArithmeticUtils::lcm)
            );
          }
        }

        List<Pulse> newPulses = modules
          .computeIfAbsent(pulse.to(), k -> new NoOp())
          .process(pulse.from(), pulse.value());
        pulses.addAll(newPulses);
      }
    }
  }

  public record Pulse(String from, String to, boolean value) {}

  public interface Module {
    public String getName();

    public List<String> getDestinations();

    public default void registerInputs(Map<String, Module> modules) {}

    public List<Pulse> process(String from, boolean pulse);
  }

  @Data
  @RequiredArgsConstructor
  public class Broadcast implements Module {

    protected final String name;
    protected final List<String> destinations;

    @Override
    public List<Pulse> process(String from, boolean pulse) {
      return destinations.stream().map(d -> new Pulse(name, d, pulse)).toList();
    }
  }

  @Data
  @RequiredArgsConstructor
  public class NoOp implements Module {

    @Override
    public String getName() {
      return "";
    }

    @Override
    public List<String> getDestinations() {
      return List.of();
    }

    @Override
    public List<Pulse> process(String from, boolean pulse) {
      return List.of();
    }
  }

  @Data
  @RequiredArgsConstructor
  public class FlipFlop implements Module {

    protected final String name;
    protected final List<String> destinations;

    protected boolean state = false;

    @Override
    public List<Pulse> process(String from, boolean pulse) {
      if (pulse) {
        return List.of();
      } else {
        state = !state;
        return destinations
          .stream()
          .map(d -> new Pulse(name, d, state))
          .toList();
      }
    }
  }

  @Data
  @RequiredArgsConstructor
  public class Conjunction implements Module {

    protected final String name;
    protected final List<String> destinations;

    protected Map<String, Boolean> lastValues = new HashMap<>();

    public void registerInputs(Map<String, Module> modules) {
      modules
        .values()
        .forEach((Module module) -> {
          if (module.getDestinations().contains(name)) {
            lastValues.put(module.getName(), false);
          }
        });
    }

    @Override
    public List<Pulse> process(String from, boolean pulse) {
      lastValues.put(from, pulse);
      if (lastValues.values().stream().allMatch(b -> b)) {
        return destinations
          .stream()
          .map(d -> new Pulse(name, d, false))
          .toList();
      } else {
        return destinations
          .stream()
          .map(d -> new Pulse(name, d, true))
          .toList();
      }
    }
  }
}
