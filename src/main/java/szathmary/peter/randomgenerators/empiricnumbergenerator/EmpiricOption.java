package szathmary.peter.randomgenerators.empiricnumbergenerator;

public record EmpiricOption<T extends Number>(T min, T max, double probability) {
  public EmpiricOption(T min, T max, double probability) {
    this.min = min;
    this.max = max;

    if (probability <= 0) {
      throw new IllegalArgumentException("Cannot initialize EmpiricOption with probability <= 0!");
    }

    if (probability >= 1) {
      throw new IllegalArgumentException(
          "Cannot initialize EmpiricalOption with probability >= 1!");
    }

    this.probability = probability;
  }
}
